package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;

public interface FuncionalidadRepository {

    FuncionalidadEntity save(FuncionalidadEntity funcionalidad);

    boolean existsByNombre(String nombre);

    Optional<FuncionalidadEntity> findById(UUID id);

    List<FuncionalidadEntity> findAll();
}