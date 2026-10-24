package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.ControlEstadoCatalogo;
import co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.vigencia.RegistroVigenciaMapper;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class ParametroCatalogoStrategy extends CatalogoCacheStrategy<ParametroEntity> implements ParametroRepository {
    private final BaseDatosParametroRepository baseDatos;
    private final ControlEstadoCatalogo controlEstado;

    public ParametroCatalogoStrategy(final CacheParametroRepository cache,
            final BaseDatosParametroRepository baseDatos, final ControlEstadoCatalogo controlEstado) {
        super(cache, LoggerFactory.getLogger(ParametroCatalogoStrategy.class), "PARAMETRO");
        this.baseDatos = baseDatos;
        this.controlEstado = controlEstado;
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
    public List<ParametroEntity> findByAplicacionAndAmbiente(final UUID aplicacionId, final UUID ambienteId) {
        var disponibles = controlEstado.parametrosDisponibles(aplicacionId, ambienteId);
        return consultar("aplicacion:" + aplicacionId + ":ambiente:" + ambienteId,
                () -> baseDatos.findByAplicacionAndAmbiente(aplicacionId, ambienteId)).stream()
                .filter(parametro -> disponibles.contains(parametro.getId())).toList();
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        // Las validaciones de unicidad siempre consultan la fuente de verdad.
        return baseDatos.existsByNombre(nombre);
    }

    @Override
    public ParametroEntity save(final ParametroEntity parametro) {
        return controlEstado.guardar(RegistroVigenciaMapper.INSTANCE.toRegistro(parametro), true, () -> {
            var resultado = baseDatos.save(parametro);
            invalidar();
            return resultado;
        });
    }

    @Override
    public ParametroEntity update(final ParametroEntity parametro) {
        return controlEstado.guardar(RegistroVigenciaMapper.INSTANCE.toRegistro(parametro), false, () -> {
            var resultado = baseDatos.update(parametro);
            invalidar();
            return resultado;
        });
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

}
