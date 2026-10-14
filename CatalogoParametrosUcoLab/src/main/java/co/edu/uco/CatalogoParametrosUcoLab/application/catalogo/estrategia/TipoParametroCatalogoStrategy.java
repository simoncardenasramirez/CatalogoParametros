package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosTipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheTipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class TipoParametroCatalogoStrategy extends CatalogoCacheStrategy<TipoParametroEntity> implements TipoParametroRepository {
    private final BaseDatosTipoParametroRepository baseDatos;

    public TipoParametroCatalogoStrategy(final CacheTipoParametroRepository cache,
            final BaseDatosTipoParametroRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(TipoParametroCatalogoStrategy.class), "TIPOPARAMETRO");
        this.baseDatos = baseDatos;
    }

    @Override
    public TipoParametroEntity save(final TipoParametroEntity tipoParametro) {
        var resultado = baseDatos.save(tipoParametro);
        invalidar();
        return resultado;
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        return baseDatos.existsByNombre(nombre);
    }

    @Override
    public Optional<TipoParametroEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<TipoParametroEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public TipoParametroEntity update(final TipoParametroEntity tipoParametro) {
        var resultado = baseDatos.update(tipoParametro);
        invalidar();
        return resultado;
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

}
