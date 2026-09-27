package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

public interface AmbienteRepository {

    AmbienteEntity save(AmbienteEntity ambiente);

    AmbienteEntity update(AmbienteEntity ambiente);

    void deleteById(UUID id);

    boolean existsByNombre(String nombre);

    Optional<AmbienteEntity> findById(UUID id);

    List<AmbienteEntity> findAll();

    List<AmbienteEntity> findAllPaginado(int pagina, int tamanoPagina);
}
