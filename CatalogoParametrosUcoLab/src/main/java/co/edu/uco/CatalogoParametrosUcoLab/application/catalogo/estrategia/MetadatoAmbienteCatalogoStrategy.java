package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class MetadatoAmbienteCatalogoStrategy extends CatalogoCacheStrategy<MetadatoAmbienteEntity> implements MetadatoAmbienteRepository {
    private final BaseDatosMetadatoAmbienteRepository baseDatos;

    public MetadatoAmbienteCatalogoStrategy(final CacheMetadatoAmbienteRepository cache,
            final BaseDatosMetadatoAmbienteRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(MetadatoAmbienteCatalogoStrategy.class), "METADATOAMBIENTE");
        this.baseDatos = baseDatos;
    }

    @Override
    public MetadatoAmbienteEntity save(final MetadatoAmbienteEntity entity) {
        var resultado = baseDatos.save(entity);
        invalidar();
        return resultado;
    }

    @Override
    public MetadatoAmbienteEntity update(final MetadatoAmbienteEntity entity) {
        var resultado = baseDatos.update(entity);
        invalidar();
        return resultado;
    }

    @Override
    public void deleteById(final UUID id) {
        baseDatos.deleteById(id);
        invalidar();
    }

    @Override
    public Optional<MetadatoAmbienteEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<MetadatoAmbienteEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<MetadatoAmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

    @Override
    public boolean existsByIdAmbiente(final UUID idAmbiente) {
        return baseDatos.existsByIdAmbiente(idAmbiente);
    }

    @Override
    public boolean existsByIdEstadoMetadatoAmbiente(final UUID idEstadoMetadatoAmbiente) {
        return baseDatos.existsByIdEstadoMetadatoAmbiente(idEstadoMetadatoAmbiente);
    }

}
