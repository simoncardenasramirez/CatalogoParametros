package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosFuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheFuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class FuncionalidadCatalogoStrategy extends CatalogoCacheStrategy<FuncionalidadEntity> implements FuncionalidadRepository {
    private final BaseDatosFuncionalidadRepository baseDatos;

    public FuncionalidadCatalogoStrategy(final CacheFuncionalidadRepository cache,
            final BaseDatosFuncionalidadRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(FuncionalidadCatalogoStrategy.class), "FUNCIONALIDAD");
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

}
