package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class MetadatoCatalogoStrategy extends CatalogoCacheStrategy<MetadatoEntity> implements MetadatoRepository {
    private final BaseDatosMetadatoRepository baseDatos;

    public MetadatoCatalogoStrategy(final CacheMetadatoRepository cache,
            final BaseDatosMetadatoRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(MetadatoCatalogoStrategy.class), "METADATO");
        this.baseDatos = baseDatos;
    }

    @Override
    public MetadatoEntity save(final MetadatoEntity metadato) {
        var resultado = baseDatos.save(metadato);
        invalidar();
        return resultado;
    }

    @Override
    public MetadatoEntity update(final MetadatoEntity metadato) {
        var resultado = baseDatos.update(metadato);
        invalidar();
        return resultado;
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

    @Override
    public Optional<MetadatoEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<MetadatoEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<MetadatoEntity> findByIdParametro(final UUID idParametro) {
        return consultar("parametro:" + idParametro, () -> baseDatos.findByIdParametro(idParametro));
    }

}
