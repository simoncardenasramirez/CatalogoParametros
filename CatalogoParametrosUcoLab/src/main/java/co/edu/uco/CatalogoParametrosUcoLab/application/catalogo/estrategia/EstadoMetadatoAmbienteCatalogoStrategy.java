package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosEstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheEstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class EstadoMetadatoAmbienteCatalogoStrategy implements EstadoMetadatoAmbienteRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(EstadoMetadatoAmbienteCatalogoStrategy.class);

    private final CacheEstadoMetadatoAmbienteRepository cache;
    private final BaseDatosEstadoMetadatoAmbienteRepository baseDatos;

    public EstadoMetadatoAmbienteCatalogoStrategy(final CacheEstadoMetadatoAmbienteRepository cache,
            final BaseDatosEstadoMetadatoAmbienteRepository baseDatos) {
        this.cache = cache;
        this.baseDatos = baseDatos;
    }

    @Override
    public EstadoMetadatoAmbienteEntity save(final EstadoMetadatoAmbienteEntity estadoMetadatoAmbiente) {
        var resultado = baseDatos.save(estadoMetadatoAmbiente);
        invalidar();
        return resultado;
    }

    @Override
    public EstadoMetadatoAmbienteEntity update(final EstadoMetadatoAmbienteEntity estadoMetadatoAmbiente) {
        var resultado = baseDatos.update(estadoMetadatoAmbiente);
        invalidar();
        return resultado;
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        return baseDatos.existsByNombre(nombre);
    }

    @Override
    public Optional<EstadoMetadatoAmbienteEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<EstadoMetadatoAmbienteEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<EstadoMetadatoAmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

    private List<EstadoMetadatoAmbienteEntity> consultar(final String clave, final Supplier<List<EstadoMetadatoAmbienteEntity>> consulta) {
        String version;
        try {
            version = cache.obtenerVersion();
            var resultado = cache.consultar(version, clave);
            if (resultado.isPresent()) {
                LOGGER.debug("[CACHE-ESTADOMETADATOAMBIENTE] Consulta resuelta desde cache: {}", clave);
                return resultado.get();
            }
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-ESTADOMETADATOAMBIENTE] Cache no disponible; se consulta la base de datos", exception);
            return consulta.get();
        }

        var resultado = consulta.get();
        try {
            // Una mutacion concurrente cambia la version: esta respuesta quedara inaccesible.
            cache.guardar(version, clave, resultado);
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-ESTADOMETADATOAMBIENTE] No fue posible poblar la cache", exception);
        }
        return resultado;
    }

    private void invalidar() {
        try {
            cache.invalidar();
        } catch (final TechnicalException exception) {
            // La transaccion ya fue confirmada; no se reporta como fallida al cliente.
            LOGGER.warn("[CACHE-ESTADOMETADATOAMBIENTE] No fue posible invalidar la cache; los datos expiran por TTL", exception);
        }
    }
}
