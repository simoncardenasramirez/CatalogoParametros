package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;

/** Replica la estrategia cache -> base de datos del catalogo de mensajes. */
@Service
public final class AmbienteCatalogoStrategy extends CatalogoCacheStrategy<AmbienteEntity> implements AmbienteRepository {
    private final BaseDatosAmbienteRepository baseDatos;

    public AmbienteCatalogoStrategy(final CacheAmbienteRepository cache,
            final BaseDatosAmbienteRepository baseDatos) {
        super(cache, LoggerFactory.getLogger(AmbienteCatalogoStrategy.class), "AMBIENTE");
        this.baseDatos = baseDatos;
    }

    @Override
    public AmbienteEntity save(final AmbienteEntity ambiente) {
        var resultado = baseDatos.save(ambiente);
        invalidar();
        return resultado;
    }

    @Override
    public AmbienteEntity update(final AmbienteEntity ambiente) {
        var resultado = baseDatos.update(ambiente);
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
    public Optional<AmbienteEntity> findById(final UUID id) {
        return consultar("id:" + id, () -> baseDatos.findById(id).map(List::of).orElseGet(List::of)).stream().findFirst();
    }

    @Override
    public List<AmbienteEntity> findAll() {
        return consultar("todos", baseDatos::findAll);
    }

    @Override
    public List<AmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        return consultar("pagina:" + pagina + ":" + tamanoPagina, () -> baseDatos.findAllPaginado(pagina, tamanoPagina));
    }

}
