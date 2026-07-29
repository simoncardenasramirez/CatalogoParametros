package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;

public interface OrganizacionRepository {

    OrganizacionEntity save(OrganizacionEntity organizacion);

    boolean existsByNombre(String nombre);

    Optional<OrganizacionEntity> findById(UUID id);

    List<OrganizacionEntity> findAll();
}
