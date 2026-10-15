package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum EstadoTokenCacheMapper {
    INSTANCE;

    public EstadoTokenCacheData toData(final EstadoTokenEntity entity) {
        return new EstadoTokenCacheData(entity.getId(), entity.getEstado());
    }

    public EstadoTokenEntity toEntity(final EstadoTokenCacheData data) {
        return EstadoTokenEntity.create(data.id(), data.estado());
    }

    public record EstadoTokenCacheData(UUID id, String estado) {
    }
}
