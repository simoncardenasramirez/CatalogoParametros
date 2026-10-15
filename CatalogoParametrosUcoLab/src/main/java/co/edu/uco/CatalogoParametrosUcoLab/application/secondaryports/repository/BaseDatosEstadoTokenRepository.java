package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;

public interface BaseDatosEstadoTokenRepository {
    Optional<EstadoTokenEntity> findById(UUID id);
    List<EstadoTokenEntity> findAll();
}
