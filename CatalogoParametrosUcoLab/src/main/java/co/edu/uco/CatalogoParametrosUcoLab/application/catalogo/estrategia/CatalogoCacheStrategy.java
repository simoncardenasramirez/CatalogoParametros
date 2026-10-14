package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheCatalogoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Politica compartida de consulta e invalidacion, dependiente solo del puerto de cache. */
public abstract class CatalogoCacheStrategy<E> {
    private final CacheCatalogoRepository<E> cache;
    private final Logger logger;
    private final String prefijoLog;

    protected CatalogoCacheStrategy(final CacheCatalogoRepository<E> cache, final Logger logger,
            final String catalogo) {
        this.cache = cache;
        this.logger = logger;
        this.prefijoLog = "[CACHE-" + catalogo + "]";
    }

    protected final List<E> consultar(final String clave, final Supplier<List<E>> consulta) {
        String version;
        try {
            version = cache.obtenerVersion();
            var resultado = cache.consultar(version, clave);
            if (resultado.isPresent()) {
                logger.debug("{} Consulta resuelta desde cache: {}", prefijoLog, clave);
                return resultado.get();
            }
        } catch (final TechnicalException exception) {
            logger.warn("{} Cache no disponible; se consulta la base de datos", prefijoLog, exception);
            return consulta.get();
        }

        var resultado = consulta.get();
        try {
            // Una mutacion concurrente cambia la version: esta respuesta quedara inaccesible.
            cache.guardar(version, clave, resultado);
        } catch (final TechnicalException exception) {
            logger.warn("{} No fue posible poblar la cache", prefijoLog, exception);
        }
        return resultado;
    }

    protected final void invalidar() {
        try {
            cache.invalidar();
        } catch (final TechnicalException exception) {
            // La transaccion ya fue confirmada; no se reporta como fallida al cliente.
            logger.warn("{} No fue posible invalidar la cache; los datos expiran por TTL", prefijoLog, exception);
        }
    }
}
