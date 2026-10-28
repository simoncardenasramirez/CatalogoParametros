package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.usecase.impl;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.ConsultarCambiosCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.CambiosCatalogoAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ConsultarCambiosCatalogoAplicacionImpl implements ConsultarCambiosCatalogoAplicacion {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsultarCambiosCatalogoAplicacionImpl.class);
    private final TelemetryService telemetry;
    private final CambiosCatalogoAplicacionPublisher cambios;
    private final BaseDatosAplicacionRepository aplicaciones;
    private final BaseDatosModuloRepository modulos;
    private final BaseDatosFuncionalidadRepository funcionalidades;
    private final BaseDatosParametroRepository parametros;
    public ConsultarCambiosCatalogoAplicacionImpl(CambiosCatalogoAplicacionPublisher cambios,
            BaseDatosAplicacionRepository aplicaciones, BaseDatosModuloRepository modulos,
            BaseDatosFuncionalidadRepository funcionalidades, BaseDatosParametroRepository parametros, TelemetryService telemetry) {
        this.telemetry = telemetry;
        this.cambios=cambios; this.aplicaciones=aplicaciones; this.modulos=modulos;
        this.funcionalidades=funcionalidades; this.parametros=parametros;
    }
    @Override public Flux<CambioCatalogoAplicacion> execute(final UUID app) {
        return Flux.defer(() -> {
            if (aplicaciones.findById(app).isEmpty()) return Flux.error(NotFoundException.build("La aplicacion no existe."));
            telemetry.recordBusinessOperation("escuchar-catalogo-aplicacion", () -> LOGGER.debug("[ESCUCHAR-CATALOGO-APLICACION] Suscripcion {}", app));
            return cambios.getStream().filter(evento -> pertenece(evento, app));
        });
    }
    private boolean pertenece(CambioCatalogoAplicacion evento, UUID app) {
        return switch(evento.recurso()) {
            case "ambientes" -> true;
            case "aplicaciones" -> app.equals(evento.id());
            case "modulos" -> app.equals(evento.idPadre());
            case "funcionalidades" -> modulo(evento.idPadre(), app);
            case "parametros" -> funcion(evento.idPadre(), app);
            case "metadatos", "metadatos-ambiente" -> parametros.findById(evento.idPadre())
                    .map(parametro -> funcion(parametro.getIdFuncionalidad(), app)).orElse(false);
            default -> false;
        };
    }
    private boolean modulo(UUID id, UUID app) { return modulos.findById(id).map(m -> app.equals(m.getIdAplicacion())).orElse(false); }
    private boolean funcion(UUID id, UUID app) { return funcionalidades.findById(id).map(f -> modulo(f.getIdModulo(), app)).orElse(false); }
}
