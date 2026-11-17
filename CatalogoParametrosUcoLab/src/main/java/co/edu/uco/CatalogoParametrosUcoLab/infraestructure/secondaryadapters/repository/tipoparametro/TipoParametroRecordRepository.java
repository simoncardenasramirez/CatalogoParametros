package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.tipoparametro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroCatalogoRecord;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

/** Adaptador de consulta para el catalogo fijo de tipos de parametro. */
@Repository
public final class TipoParametroRecordRepository implements TipoParametroRepository {

    @Override
    public Optional<TipoParametroEntity> findById(final UUID id) {
        return entidades().stream()
                .filter(tipoParametro -> tipoParametro.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<TipoParametroEntity> findAll() {
        return entidades();
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        var nombreNormalizado = nombre == null ? "" : nombre.trim();
        return entidades().stream().anyMatch(tipoParametro -> tipoParametro.getNombre().equalsIgnoreCase(nombreNormalizado));
    }

    @Override
    public TipoParametroEntity save(final TipoParametroEntity tipoParametro) {
        throw catalogoSoloLectura();
    }

    @Override
    public TipoParametroEntity update(final TipoParametroEntity tipoParametro) {
        throw catalogoSoloLectura();
    }

    @Override
    public void deleteById(final UUID id) {
        throw catalogoSoloLectura();
    }

    private List<TipoParametroEntity> entidades() {
        return TipoParametroCatalogoRecord.valores().stream()
                .map(TipoParametroCatalogoRecord::toEntity)
                .toList();
    }

    private TechnicalException catalogoSoloLectura() {
        return TechnicalException.build("El catalogo de tipos de parametro es fijo y solo permite consultas.");
    }
}
