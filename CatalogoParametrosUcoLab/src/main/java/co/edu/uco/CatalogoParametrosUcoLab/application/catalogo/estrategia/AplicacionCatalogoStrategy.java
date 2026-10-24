package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.ControlEstadoCatalogo;
import co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.vigencia.RegistroVigenciaMapper;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosAplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheAplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class AplicacionCatalogoStrategy extends CatalogoCacheStrategy<AplicacionEntity> implements AplicacionRepository {
    private final BaseDatosAplicacionRepository baseDatos;
    private final ControlEstadoCatalogo controlEstado;

    public AplicacionCatalogoStrategy(final CacheAplicacionRepository cache,
            final BaseDatosAplicacionRepository baseDatos, final ControlEstadoCatalogo controlEstado) {
        super(cache, LoggerFactory.getLogger(AplicacionCatalogoStrategy.class), "APLICACION");
        this.baseDatos = baseDatos;
        this.controlEstado = controlEstado;
    }

    @Override
    public AplicacionEntity save(final AplicacionEntity aplicacion) {
        return controlEstado.guardar(RegistroVigenciaMapper.INSTANCE.toRegistro(aplicacion), true, () -> {
            var resultado = baseDatos.save(aplicacion);
            invalidar();
            return resultado;
        });
    }

    @Override
    public AplicacionEntity update(final AplicacionEntity aplicacion) {
        return controlEstado.guardar(RegistroVigenciaMapper.INSTANCE.toRegistro(aplicacion), false, () -> {
            var resultado = baseDatos.update(aplicacion);
            invalidar();
            return resultado;
        });
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

}
