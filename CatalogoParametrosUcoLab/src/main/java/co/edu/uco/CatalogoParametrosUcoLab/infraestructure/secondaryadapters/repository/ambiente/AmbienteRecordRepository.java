package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.ambiente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteCatalogoRecord;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;

/** Adaptador de consulta para el catalogo fijo de ambientes. */
@Repository
public final class AmbienteRecordRepository implements AmbienteRepository {

    @Override
    public Optional<AmbienteEntity> findById(final UUID id) {
        return entidades().stream()
                .filter(ambiente -> ambiente.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<AmbienteEntity> findAll() {
        return entidades();
    }

    @Override
    public List<AmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        var paginaNormalizada = Math.max(pagina, 1);
        var tamanoNormalizado = Math.max(tamanoPagina, 1);
        var ambientes = entidades();
        var inicio = (paginaNormalizada - 1) * tamanoNormalizado;
        if (inicio >= ambientes.size()) {
            return List.of();
        }
        var fin = Math.min(inicio + tamanoNormalizado, ambientes.size());
        return ambientes.subList(inicio, fin);
    }

    private List<AmbienteEntity> entidades() {
        return AmbienteCatalogoRecord.valores().stream()
                .map(AmbienteCatalogoRecord::toEntity)
                .toList();
    }
}
