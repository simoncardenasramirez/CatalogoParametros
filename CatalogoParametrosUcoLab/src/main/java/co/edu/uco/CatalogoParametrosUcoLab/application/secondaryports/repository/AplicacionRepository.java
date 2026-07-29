package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;

public interface AplicacionRepository {

    AplicacionEntity save(AplicacionEntity aplicacion);

    boolean existsByNombre(String nombre);

    Optional<AplicacionEntity> findById(UUID id);

    List<AplicacionEntity> findAll();
}
