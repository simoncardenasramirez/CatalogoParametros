package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class ParametroCatalogoStrategy implements ParametroRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParametroCatalogoStrategy.class);

    private final CacheParametroRepository cache;
    private final BaseDatosParametroRepository baseDatos;

    public ParametroCatalogoStrategy(final CacheParametroRepository cache,
            final BaseDatosParametroRepository baseDatos) {
        this.cache = cache;
        this.baseDatos = baseDatos;
    }

    @Override
    public Optional<ParametroEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of))
                .stream().findFirst();
    }

    @Override
    public List<ParametroEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<ParametroEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina,
                () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

    @Override
    public List<ParametroEntity> findByIdFuncionalidad(final UUID idFuncionalidad) {
        return consultar("funcionalidad:" + idFuncionalidad,
                () -> baseDatos.findByIdFuncionalidad(idFuncionalidad));
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        // Las validaciones de unicidad siempre consultan la fuente de verdad.
        return baseDatos.existsByNombre(nombre);
    }

    @Override
    public ParametroEntity save(final ParametroEntity parametro) {
        var resultado = baseDatos.save(parametro);
        invalidar();
        return resultado;
    }

    @Override
    public ParametroEntity update(final ParametroEntity parametro) {
        var resultado = baseDatos.update(parametro);
        invalidar();
        return resultado;
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

    private List<ParametroEntity> consultar(final String clave, final Supplier<List<ParametroEntity>> consulta) {
        String version;
        try {
            version = cache.obtenerVersion();
            var resultado = cache.consultar(version, clave);
            if (resultado.isPresent()) {
                LOGGER.debug("[CACHE-PARAMETRO] Consulta resuelta desde cache: {}", clave);
                return resultado.get();
            }
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-PARAMETRO] Cache no disponible; se consulta la base de datos", exception);
            return consulta.get();
        }

        var resultado = consulta.get();
        try {
            // Una mutacion concurrente cambia la version: esta respuesta quedara inaccesible.
            cache.guardar(version, clave, resultado);
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-PARAMETRO] No fue posible poblar la cache", exception);
        }
        return resultado;
    }

    private void invalidar() {
        try {
            cache.invalidar();
        } catch (final TechnicalException exception) {
            // La transaccion ya fue confirmada; no se reporta como fallida al cliente.
            LOGGER.warn("[CACHE-PARAMETRO] No fue posible invalidar la cache; los datos expiran por TTL", exception);
        }
    }
}
