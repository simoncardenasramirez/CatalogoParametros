package co.edu.uco.CatalogoParametrosUcoLab.application.features.vigencia.usecase;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.vigencia.EvaluadorVigencia;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.vigencia.GestionarEstadosCatalogo;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.CambiosEstadoCatalogoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

@Service
public final class GestionarEstadosCatalogoImpl implements GestionarEstadosCatalogo {
    private static final Logger LOGGER = LoggerFactory.getLogger(GestionarEstadosCatalogoImpl.class);
    private final EstadoCatalogoRepository repository;
    private final List<CacheCatalogoRepository<?>> caches;
    private final CambiosEstadoCatalogoPublisher publisher;
    private final TelemetryService telemetry;
    private final Clock reloj;

    public GestionarEstadosCatalogoImpl(final EstadoCatalogoRepository repository,
            final List<CacheCatalogoRepository<?>> caches, final CambiosEstadoCatalogoPublisher publisher,
            final TelemetryService telemetry, final Clock reloj) {
        this.repository = repository;
        this.caches = List.copyOf(caches);
        this.publisher = publisher;
        this.telemetry = telemetry;
        this.reloj = reloj;
    }

    @Override
    public synchronized <T> T guardar(final RegistroVigencia registro, final boolean nuevo,
            final java.util.function.Supplier<T> persistir) {
        var evaluador = new EvaluadorVigencia(repository.consultar(), reloj.instant());
        evaluador.validar(registro, nuevo);
        var anterior = evaluador.buscar(registro.tipo(), registro.id());
        var propagar = !nuevo && anterior != null && evaluador.activo(anterior) && !evaluador.activo(registro)
                && switch (registro.tipo()) {
                    case APLICACION, MODULO, FUNCIONALIDAD, PARAMETRO -> true;
                    default -> false;
                };
        var resultado = persistir.get();
        if (propagar) sincronizar();
        return resultado;
    }

    @Override
    public synchronized void sincronizar() {
        telemetry.recordBusinessOperation("sincronizar-vigencias", () -> {
            var normalizadas = repository.normalizarFechasUtc();
            if (normalizadas > 0) LOGGER.info("[VIGENCIA-CATALOGO] Normalizadas {} fechas a UTC", normalizadas);
            var catalogo = repository.consultar();
            var evaluador = new EvaluadorVigencia(catalogo, reloj.instant());
            var cambios = catalogo.registros().stream().filter(evaluador::requiereDesactivacion).toList();
            if (cambios.isEmpty()) return;
            var estadoAsociacion = inactivo(catalogo.estadosAsociacion(), "metadato-ambiente");
            var estadoToken = inactivo(catalogo.estadosToken(), "token");
            repository.desactivar(cambios, estadoAsociacion, estadoToken);
            for (var cache : caches) {
                try { cache.invalidar(); }
                catch (final TechnicalException exception) {
                    LOGGER.warn("[VIGENCIA-CATALOGO] No fue posible invalidar una cache", exception);
                }
            }
            try { publisher.publicar(cambios); }
            catch (final RuntimeException exception) {
                // La persistencia ya fue confirmada; no falsear el resultado de la operacion.
                LOGGER.error("[VIGENCIA-CATALOGO] No fue posible notificar los cambios", exception);
            }
            LOGGER.info("[VIGENCIA-CATALOGO] Desactivados {} registros por vencimiento o dependencia", cambios.size());
        });
    }

    private UUID inactivo(final Map<UUID, String> estados, final String tipo) {
        return estados.entrySet().stream().filter(e -> "inactivo".equalsIgnoreCase(e.getValue().trim()))
                .map(Map.Entry::getKey).sorted().findFirst()
                .orElseGet(() -> UUID.nameUUIDFromBytes(("catalogo:" + tipo + ":inactivo").getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void validarRegistro(final RegistroVigencia registro, final boolean nuevo) {
        new EvaluadorVigencia(repository.consultar(), reloj.instant()).validar(registro, nuevo);
    }
    @Override
    public boolean disponible(final TipoRegistroVigencia tipo, final UUID id) {
        return new EvaluadorVigencia(repository.consultar(), reloj.instant()).disponible(tipo, id);
    }
    @Override
    public Set<UUID> parametrosDisponibles(final UUID aplicacion, final UUID ambiente) {
        return new EvaluadorVigencia(repository.consultar(), reloj.instant()).parametrosDisponibles(aplicacion, ambiente);
    }
}
