package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosAplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheAplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class AplicacionCatalogoStrategy implements AplicacionRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AplicacionCatalogoStrategy.class);

    private final CacheAplicacionRepository cache;
    private final BaseDatosAplicacionRepository baseDatos;

    public AplicacionCatalogoStrategy(final CacheAplicacionRepository cache,
            final BaseDatosAplicacionRepository baseDatos) {
        this.cache = cache;
        this.baseDatos = baseDatos;
    }

    @Override
    public AplicacionEntity save(final AplicacionEntity aplicacion) {
        var resultado = baseDatos.save(aplicacion);
        invalidar();
        return resultado;
    }

    @Override
    public AplicacionEntity update(final AplicacionEntity aplicacion) {
        var resultado = baseDatos.update(aplicacion);
        invalidar();
        return resultado;
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        return baseDatos.existsByNombre(nombre);
    }

    @Override
    public Optional<AplicacionEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<AplicacionEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<AplicacionEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

    @Override
    public boolean existsByIdOrganizacion(final UUID idOrganizacion) {
        return baseDatos.existsByIdOrganizacion(idOrganizacion);
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

    private List<AplicacionEntity> consultar(final String clave, final Supplier<List<AplicacionEntity>> consulta) {
        String version;
        try {
            version = cache.obtenerVersion();
            var resultado = cache.consultar(version, clave);
            if (resultado.isPresent()) {
                LOGGER.debug("[CACHE-APLICACION] Consulta resuelta desde cache: {}", clave);
                return resultado.get();
            }
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-APLICACION] Cache no disponible; se consulta la base de datos", exception);
            return consulta.get();
        }

        var resultado = consulta.get();
        try {
            // Una mutacion concurrente cambia la version: esta respuesta quedara inaccesible.
            cache.guardar(version, clave, resultado);
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-APLICACION] No fue posible poblar la cache", exception);
        }
        return resultado;
    }

    private void invalidar() {
        try {
            cache.invalidar();
        } catch (final TechnicalException exception) {
            // La transaccion ya fue confirmada; no se reporta como fallida al cliente.
            LOGGER.warn("[CACHE-APLICACION] No fue posible invalidar la cache; los datos expiran por TTL", exception);
        }
    }
}
