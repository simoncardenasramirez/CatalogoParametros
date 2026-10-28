package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.ControlEstadoCatalogo;
import co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.vigencia.RegistroVigenciaMapper;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class ModuloCatalogoStrategy extends CatalogoCacheStrategy<ModuloEntity> implements ModuloRepository {
    private final BaseDatosModuloRepository baseDatos;
    private final ControlEstadoCatalogo controlEstado;

    public ModuloCatalogoStrategy(final CacheModuloRepository cache,
            final BaseDatosModuloRepository baseDatos, final ControlEstadoCatalogo controlEstado) {
        super(cache, LoggerFactory.getLogger(ModuloCatalogoStrategy.class), "MODULO");
        this.baseDatos = baseDatos;
        this.controlEstado = controlEstado;
    }

    @Override
    public ModuloEntity save(final ModuloEntity modulo) {
        return controlEstado.guardar(RegistroVigenciaMapper.INSTANCE.toRegistro(modulo), true, () -> {
            var resultado = baseDatos.save(modulo);
            invalidar();
            return resultado;
        });
    }

    @Override
    public ModuloEntity update(final ModuloEntity modulo) {
        return controlEstado.guardar(RegistroVigenciaMapper.INSTANCE.toRegistro(modulo), false, () -> {
            var resultado = baseDatos.update(modulo);
            invalidar();
            return resultado;
        });
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

}
