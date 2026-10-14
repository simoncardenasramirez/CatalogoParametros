package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosOrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheOrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class OrganizacionCatalogoStrategy extends CatalogoCacheStrategy<OrganizacionEntity> implements OrganizacionRepository {
    private final BaseDatosOrganizacionRepository baseDatos;

    public OrganizacionCatalogoStrategy(final CacheOrganizacionRepository cache,
            final BaseDatosOrganizacionRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(OrganizacionCatalogoStrategy.class), "ORGANIZACION");
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

}
