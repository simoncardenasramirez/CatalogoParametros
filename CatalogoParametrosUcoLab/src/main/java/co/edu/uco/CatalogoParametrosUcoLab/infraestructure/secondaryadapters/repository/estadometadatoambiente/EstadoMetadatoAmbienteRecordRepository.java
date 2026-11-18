package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.estadometadatoambiente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteCatalogoRecord;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;

/** Adaptador de consulta para el catalogo fijo de estados de metadatos por ambiente. */
@Repository
public final class EstadoMetadatoAmbienteRecordRepository implements EstadoMetadatoAmbienteRepository {

    @Override
    public Optional<EstadoMetadatoAmbienteEntity> findById(final UUID id) {
        return entidades().stream()
                .filter(estado -> estado.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<EstadoMetadatoAmbienteEntity> findAll() {
        return entidades();
    }

    @Override
    public List<EstadoMetadatoAmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        var paginaNormalizada = Math.max(pagina, 1);
        var tamanoNormalizado = Math.max(tamanoPagina, 1);
        var estados = entidades();
        var inicio = (paginaNormalizada - 1) * tamanoNormalizado;
        if (inicio >= estados.size()) {
            return List.of();
        }
        var fin = Math.min(inicio + tamanoNormalizado, estados.size());
        return estados.subList(inicio, fin);
    }

    private List<EstadoMetadatoAmbienteEntity> entidades() {
        return EstadoMetadatoAmbienteCatalogoRecord.valores().stream()
                .map(EstadoMetadatoAmbienteCatalogoRecord::toEntity)
                .toList();
    }
}
