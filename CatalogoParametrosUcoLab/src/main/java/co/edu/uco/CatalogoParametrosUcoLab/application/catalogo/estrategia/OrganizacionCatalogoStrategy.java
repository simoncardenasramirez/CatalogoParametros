package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosOrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheOrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class OrganizacionCatalogoStrategy implements OrganizacionRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizacionCatalogoStrategy.class);

    private final CacheOrganizacionRepository cache;
    private final BaseDatosOrganizacionRepository baseDatos;

    public OrganizacionCatalogoStrategy(final CacheOrganizacionRepository cache,
            final BaseDatosOrganizacionRepository baseDatos) {
        this.cache = cache;
        this.baseDatos = baseDatos;
    }

    @Override
    public OrganizacionEntity save(final OrganizacionEntity organizacion) {
        var resultado = baseDatos.save(organizacion);
        invalidar();
        return resultado;
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        return baseDatos.existsByNombre(nombre);
    }

    @Override
    public Optional<OrganizacionEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<OrganizacionEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<OrganizacionEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

    @Override
    public OrganizacionEntity update(final OrganizacionEntity organizacion) {
        var resultado = baseDatos.update(organizacion);
        invalidar();
        return resultado;
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

    private List<OrganizacionEntity> consultar(final String clave, final Supplier<List<OrganizacionEntity>> consulta) {
        String version;
        try {
            version = cache.obtenerVersion();
            var resultado = cache.consultar(version, clave);
            if (resultado.isPresent()) {
                LOGGER.debug("[CACHE-ORGANIZACION] Consulta resuelta desde cache: {}", clave);
                return resultado.get();
            }
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-ORGANIZACION] Cache no disponible; se consulta la base de datos", exception);
            return consulta.get();
        }

        var resultado = consulta.get();
        try {
            // Una mutacion concurrente cambia la version: esta respuesta quedara inaccesible.
            cache.guardar(version, clave, resultado);
        } catch (final TechnicalException exception) {
            LOGGER.warn("[CACHE-ORGANIZACION] No fue posible poblar la cache", exception);
        }
        return resultado;
    }

    private void invalidar() {
        try {
            cache.invalidar();
        } catch (final TechnicalException exception) {
            // La transaccion ya fue confirmada; no se reporta como fallida al cliente.
            LOGGER.warn("[CACHE-ORGANIZACION] No fue posible invalidar la cache; los datos expiran por TTL", exception);
        }
    }
}
