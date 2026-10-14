package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum EstadoMetadatoAmbienteCacheMapper {
    INSTANCE;

    public EstadoMetadatoAmbienteCacheData toData(final EstadoMetadatoAmbienteEntity entity) {
        return new EstadoMetadatoAmbienteCacheData(entity.getId(), entity.getNombre());
    }

    public EstadoMetadatoAmbienteEntity toEntity(final EstadoMetadatoAmbienteCacheData data) {
        return EstadoMetadatoAmbienteEntity.create(data.id(), data.nombre());
    }

    public record EstadoMetadatoAmbienteCacheData(UUID id, String nombre) {
    }
}
