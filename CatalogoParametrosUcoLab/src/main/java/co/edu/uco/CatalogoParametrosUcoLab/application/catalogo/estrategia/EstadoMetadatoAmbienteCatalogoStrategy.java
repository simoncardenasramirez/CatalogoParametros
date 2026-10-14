package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosEstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheEstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class EstadoMetadatoAmbienteCatalogoStrategy extends CatalogoCacheStrategy<EstadoMetadatoAmbienteEntity> implements EstadoMetadatoAmbienteRepository {
    private final BaseDatosEstadoMetadatoAmbienteRepository baseDatos;

    public EstadoMetadatoAmbienteCatalogoStrategy(final CacheEstadoMetadatoAmbienteRepository cache,
            final BaseDatosEstadoMetadatoAmbienteRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(EstadoMetadatoAmbienteCatalogoStrategy.class), "ESTADOMETADATOAMBIENTE");
        this.baseDatos = baseDatos;
    }

    @Override
    public EstadoMetadatoAmbienteEntity save(final EstadoMetadatoAmbienteEntity estadoMetadatoAmbiente) {
        var resultado = baseDatos.save(estadoMetadatoAmbiente);
        invalidar();
        return resultado;
    }

    @Override
    public EstadoMetadatoAmbienteEntity update(final EstadoMetadatoAmbienteEntity estadoMetadatoAmbiente) {
        var resultado = baseDatos.update(estadoMetadatoAmbiente);
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
    public Optional<EstadoMetadatoAmbienteEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<EstadoMetadatoAmbienteEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<EstadoMetadatoAmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

}
