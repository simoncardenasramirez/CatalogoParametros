package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class MetadatoCatalogoStrategy implements MetadatoRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadatoCatalogoStrategy.class);

    private final CacheMetadatoRepository cache;
    private final BaseDatosMetadatoRepository baseDatos;

    public MetadatoCatalogoStrategy(final CacheMetadatoRepository cache,
            final BaseDatosMetadatoRepository baseDatos) {
        this.cache = cache;
        this.baseDatos = baseDatos;
    }

    @Override
    public MetadatoEntity save(final MetadatoEntity metadato) {
        var resultado = baseDatos.save(metadato);
        invalidar();
        return resultado;
    }

    @Override
    public MetadatoEntity update(final MetadatoEntity metadato) {
        var resultado = baseDatos.update(metadato);
        invalidar();
        return resultado;
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

    @Override
    public Optional<MetadatoEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<MetadatoEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<MetadatoEntity> findByIdParametro(final UUID idParametro) {
        return consultar("parametro:" + idParametro, () -> baseDatos.findByIdParametro(idParametro));
    }

    private List<MetadatoEntity> consultar(final String clave, final Supplier<List<MetadatoEntity>> consulta) {
        String version;
        try {
            version = cache.obtenerVersion();
            var resultado = cache.consultar(version, clave);
            if (resultado.isPresent()) {
                LOGGER.debug("[CACHE-METADATO] Consulta resuelta desde cache: {}", clave);
                return resultado.get();
            }
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-METADATO] Cache no disponible; se consulta la base de datos", exception);
            return consulta.get();
        }

        var resultado = consulta.get();
        try {
            // Una mutacion concurrente cambia la version: esta respuesta quedara inaccesible.
            cache.guardar(version, clave, resultado);
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-METADATO] No fue posible poblar la cache", exception);
        }
        return resultado;
    }

    private void invalidar() {
        try {
            cache.invalidar();
        } catch (final TechnicalException exception) {
            // La transaccion ya fue confirmada; no se reporta como fallida al cliente.
            LOGGER.warn("[CACHE-METADATO] No fue posible invalidar la cache; los datos expiran por TTL", exception);
        }
    }
}
