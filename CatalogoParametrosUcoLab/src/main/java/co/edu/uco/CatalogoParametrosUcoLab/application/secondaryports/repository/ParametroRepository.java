package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

public interface ParametroRepository {

    ParametroEntity save(ParametroEntity parametro);

    boolean existsByNombre(String nombre);

    Optional<ParametroEntity> findById(UUID id);

    List<ParametroEntity> findAll();
}
