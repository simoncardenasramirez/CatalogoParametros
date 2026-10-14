package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class MetadatoAmbienteCatalogoStrategy implements MetadatoAmbienteRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadatoAmbienteCatalogoStrategy.class);

    private final CacheMetadatoAmbienteRepository cache;
    private final BaseDatosMetadatoAmbienteRepository baseDatos;

    public MetadatoAmbienteCatalogoStrategy(final CacheMetadatoAmbienteRepository cache,
            final BaseDatosMetadatoAmbienteRepository baseDatos) {
        this.cache = cache;
        this.baseDatos = baseDatos;
    }

    @Override
    public MetadatoAmbienteEntity save(final MetadatoAmbienteEntity entity) {
        var resultado = baseDatos.save(entity);
        invalidar();
        return resultado;
    }

    @Override
    public MetadatoAmbienteEntity update(final MetadatoAmbienteEntity entity) {
        var resultado = baseDatos.update(entity);
        invalidar();
        return resultado;
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

    @Override
    public Optional<MetadatoAmbienteEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<MetadatoAmbienteEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<MetadatoAmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

    @Override
    public boolean existsByIdAmbiente(final UUID idAmbiente) {
        return baseDatos.existsByIdAmbiente(idAmbiente);
    }

    @Override
    public boolean existsByIdEstadoMetadatoAmbiente(final UUID idEstadoMetadatoAmbiente) {
        return baseDatos.existsByIdEstadoMetadatoAmbiente(idEstadoMetadatoAmbiente);
    }

    private List<MetadatoAmbienteEntity> consultar(final String clave, final Supplier<List<MetadatoAmbienteEntity>> consulta) {
        String version;
        try {
            version = cache.obtenerVersion();
            var resultado = cache.consultar(version, clave);
            if (resultado.isPresent()) {
                LOGGER.debug("[CACHE-METADATOAMBIENTE] Consulta resuelta desde cache: {}", clave);
                return resultado.get();
            }
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-METADATOAMBIENTE] Cache no disponible; se consulta la base de datos", exception);
            return consulta.get();
        }

        var resultado = consulta.get();
        try {
            // Una mutacion concurrente cambia la version: esta respuesta quedara inaccesible.
            cache.guardar(version, clave, resultado);
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-METADATOAMBIENTE] No fue posible poblar la cache", exception);
        }
        return resultado;
    }

    private void invalidar() {
        try {
            cache.invalidar();
        } catch (final TechnicalException exception) {
            // La transaccion ya fue confirmada; no se reporta como fallida al cliente.
            LOGGER.warn("[CACHE-METADATOAMBIENTE] No fue posible invalidar la cache; los datos expiran por TTL", exception);
        }
    }
}
