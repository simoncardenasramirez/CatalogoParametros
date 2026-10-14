package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosTipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheTipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoMetadatoRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class TipoMetadatoCatalogoStrategy extends CatalogoCacheStrategy<TipoMetadatoEntity> implements TipoMetadatoRepository {
    private final BaseDatosTipoMetadatoRepository baseDatos;

    public TipoMetadatoCatalogoStrategy(final CacheTipoMetadatoRepository cache,
            final BaseDatosTipoMetadatoRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(TipoMetadatoCatalogoStrategy.class), "TIPOMETADATO");
        this.baseDatos = baseDatos;
    }

    @Override
    public Optional<TipoMetadatoEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<TipoMetadatoEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

}
