package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

public interface MetadatoAmbienteRepository {
    MetadatoAmbienteEntity save(MetadatoAmbienteEntity entity);
    MetadatoAmbienteEntity update(MetadatoAmbienteEntity entity);
    void deleteById(UUID id);
    Optional<MetadatoAmbienteEntity> findById(UUID id);
    List<MetadatoAmbienteEntity> findAll();
    List<MetadatoAmbienteEntity> findAllPaginado(int pagina, int tamanoPagina);
    boolean existsByIdAmbiente(UUID idAmbiente);
    boolean existsByIdEstadoMetadatoAmbiente(UUID idEstadoMetadatoAmbiente);
}
