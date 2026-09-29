package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

public interface EstadoAmbienteRepository {

    EstadoAmbienteEntity save(EstadoAmbienteEntity estadoAmbiente);

    EstadoAmbienteEntity update(EstadoAmbienteEntity estadoAmbiente);

    void deleteById(UUID id);

    boolean existsByNombre(String nombre);

    Optional<EstadoAmbienteEntity> findById(UUID id);

    List<EstadoAmbienteEntity> findAll();

    List<EstadoAmbienteEntity> findAllPaginado(int pagina, int tamanoPagina);
}
