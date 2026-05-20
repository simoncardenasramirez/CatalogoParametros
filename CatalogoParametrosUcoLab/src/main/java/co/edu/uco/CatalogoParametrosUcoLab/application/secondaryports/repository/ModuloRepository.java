package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;

public interface ModuloRepository {

    ModuloEntity save(ModuloEntity modulo);

    boolean existsByNombre(String nombre);

    Optional<ModuloEntity> findById(UUID id);

    List<ModuloEntity> findAll();
}
