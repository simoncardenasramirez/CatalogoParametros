package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class ModuloCatalogoStrategy implements ModuloRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuloCatalogoStrategy.class);

    private final CacheModuloRepository cache;
    private final BaseDatosModuloRepository baseDatos;

    public ModuloCatalogoStrategy(final CacheModuloRepository cache,
            final BaseDatosModuloRepository baseDatos) {
        this.cache = cache;
        this.baseDatos = baseDatos;
    }

    @Override
    public ModuloEntity save(final ModuloEntity modulo) {
        var resultado = baseDatos.save(modulo);
        invalidar();
        return resultado;
    }

    @Override
    public ModuloEntity update(final ModuloEntity modulo) {
        var resultado = baseDatos.update(modulo);
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
    public Optional<ModuloEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<ModuloEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<ModuloEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

    @Override
    public boolean existsByIdAplicacion(final UUID idAplicacion) {
        return baseDatos.existsByIdAplicacion(idAplicacion);
    }

    private List<ModuloEntity> consultar(final String clave, final Supplier<List<ModuloEntity>> consulta) {
        String version;
        try {
            version = cache.obtenerVersion();
            var resultado = cache.consultar(version, clave);
            if (resultado.isPresent()) {
                LOGGER.debug("[CACHE-MODULO] Consulta resuelta desde cache: {}", clave);
                return resultado.get();
            }
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-MODULO] Cache no disponible; se consulta la base de datos", exception);
            return consulta.get();
        }

        var resultado = consulta.get();
        try {
            // Una mutacion concurrente cambia la version: esta respuesta quedara inaccesible.
            cache.guardar(version, clave, resultado);
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-MODULO] No fue posible poblar la cache", exception);
        }
        return resultado;
    }

    private void invalidar() {
        try {
            cache.invalidar();
        } catch (final TechnicalException exception) {
            // La transaccion ya fue confirmada; no se reporta como fallida al cliente.
            LOGGER.warn("[CACHE-MODULO] No fue posible invalidar la cache; los datos expiran por TTL", exception);
        }
    }
}
