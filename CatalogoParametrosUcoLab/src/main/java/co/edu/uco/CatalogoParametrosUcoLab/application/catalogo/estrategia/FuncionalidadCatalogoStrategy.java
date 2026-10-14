package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosFuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheFuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class FuncionalidadCatalogoStrategy implements FuncionalidadRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionalidadCatalogoStrategy.class);

    private final CacheFuncionalidadRepository cache;
    private final BaseDatosFuncionalidadRepository baseDatos;

    public FuncionalidadCatalogoStrategy(final CacheFuncionalidadRepository cache,
            final BaseDatosFuncionalidadRepository baseDatos) {
        this.cache = cache;
        this.baseDatos = baseDatos;
    }

    @Override
    public FuncionalidadEntity save(final FuncionalidadEntity funcionalidad) {
        var resultado = baseDatos.save(funcionalidad);
        invalidar();
        return resultado;
    }

    @Override
    public FuncionalidadEntity update(final FuncionalidadEntity funcionalidad) {
        var resultado = baseDatos.update(funcionalidad);
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
    public Optional<FuncionalidadEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<FuncionalidadEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<FuncionalidadEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

    private List<FuncionalidadEntity> consultar(final String clave, final Supplier<List<FuncionalidadEntity>> consulta) {
        String version;
        try {
            version = cache.obtenerVersion();
            var resultado = cache.consultar(version, clave);
            if (resultado.isPresent()) {
                LOGGER.debug("[CACHE-FUNCIONALIDAD] Consulta resuelta desde cache: {}", clave);
                return resultado.get();
            }
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-FUNCIONALIDAD] Cache no disponible; se consulta la base de datos", exception);
            return consulta.get();
        }

        var resultado = consulta.get();
        try {
            // Una mutacion concurrente cambia la version: esta respuesta quedara inaccesible.
            cache.guardar(version, clave, resultado);
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-FUNCIONALIDAD] No fue posible poblar la cache", exception);
        }
        return resultado;
    }

    private void invalidar() {
        try {
            cache.invalidar();
        } catch (final TechnicalException exception) {
            // La transaccion ya fue confirmada; no se reporta como fallida al cliente.
            LOGGER.warn("[CACHE-FUNCIONALIDAD] No fue posible invalidar la cache; los datos expiran por TTL", exception);
        }
    }
}
