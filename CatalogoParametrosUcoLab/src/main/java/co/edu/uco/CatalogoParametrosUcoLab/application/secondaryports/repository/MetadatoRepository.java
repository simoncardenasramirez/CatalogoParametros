package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;

public interface MetadatoRepository {
    MetadatoEntity save(MetadatoEntity metadato);
    MetadatoEntity update(MetadatoEntity metadato);
    void deleteById(UUID id);
    Optional<MetadatoEntity> findById(UUID id);
    List<MetadatoEntity> findAll();
    List<MetadatoEntity> findByIdParametro(UUID idParametro);
}
