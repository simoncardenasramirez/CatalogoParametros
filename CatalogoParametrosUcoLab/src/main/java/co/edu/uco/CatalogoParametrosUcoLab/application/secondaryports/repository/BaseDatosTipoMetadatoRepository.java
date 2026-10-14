package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;

public interface BaseDatosTipoMetadatoRepository {
    Optional<TipoMetadatoEntity> findById(UUID id);
    List<TipoMetadatoEntity> findAll();
}
