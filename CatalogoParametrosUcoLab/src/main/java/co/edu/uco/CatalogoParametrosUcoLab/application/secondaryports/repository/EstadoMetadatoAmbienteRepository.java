package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

public interface EstadoMetadatoAmbienteRepository {

    EstadoMetadatoAmbienteEntity save(EstadoMetadatoAmbienteEntity estadoMetadatoAmbiente);

    EstadoMetadatoAmbienteEntity update(EstadoMetadatoAmbienteEntity estadoMetadatoAmbiente);

    void deleteById(UUID id);

    boolean existsByNombre(String nombre);

    Optional<EstadoMetadatoAmbienteEntity> findById(UUID id);

    List<EstadoMetadatoAmbienteEntity> findAll();

    List<EstadoMetadatoAmbienteEntity> findAllPaginado(int pagina, int tamanoPagina);
}
