package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosEstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheEstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoTokenRepository;

/** Estrategia cache -> base de datos para el catalogo de estados de token. */
@Service
public final class EstadoTokenCatalogoStrategy extends CatalogoCacheStrategy<EstadoTokenEntity>
        implements EstadoTokenRepository {
    private final BaseDatosEstadoTokenRepository baseDatos;

    public EstadoTokenCatalogoStrategy(final CacheEstadoTokenRepository cache,
            final BaseDatosEstadoTokenRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(EstadoTokenCatalogoStrategy.class), "ESTADOTOKEN");
        this.baseDatos = baseDatos;
    }

    @Override
    public Optional<EstadoTokenEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of))
                .stream().findFirst();
    }

    @Override
    public List<EstadoTokenEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }
}
