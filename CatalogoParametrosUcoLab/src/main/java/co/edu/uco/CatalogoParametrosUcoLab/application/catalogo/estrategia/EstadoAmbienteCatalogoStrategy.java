package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosEstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheEstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class EstadoAmbienteCatalogoStrategy extends CatalogoCacheStrategy<EstadoAmbienteEntity> implements EstadoAmbienteRepository {
    private final BaseDatosEstadoAmbienteRepository baseDatos;

    public EstadoAmbienteCatalogoStrategy(final CacheEstadoAmbienteRepository cache,
            final BaseDatosEstadoAmbienteRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(EstadoAmbienteCatalogoStrategy.class), "ESTADOAMBIENTE");
        this.baseDatos = baseDatos;
    }

    @Override
    public EstadoAmbienteEntity save(final EstadoAmbienteEntity estadoAmbiente) {
        var resultado = baseDatos.save(estadoAmbiente);
        invalidar();
        return resultado;
    }

    @Override
    public EstadoAmbienteEntity update(final EstadoAmbienteEntity estadoAmbiente) {
        var resultado = baseDatos.update(estadoAmbiente);
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
    public Optional<EstadoAmbienteEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<EstadoAmbienteEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<EstadoAmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

}
