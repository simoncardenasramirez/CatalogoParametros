package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;

public interface IdentidadServicioRepository {
    IdentidadServicioEntity save(IdentidadServicioEntity entity);
    List<IdentidadServicioEntity> findByIdAplicacion(UUID idAplicacion);
    Optional<IdentidadServicioEntity> findByClientId(String clientId);
    Optional<IdentidadServicioEntity> findByIdAplicacionAndClientId(UUID idAplicacion, String clientId);
    void deleteById(UUID id);
}
