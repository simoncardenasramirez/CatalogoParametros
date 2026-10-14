package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum EstadoAmbienteCacheMapper {
    INSTANCE;

    public EstadoAmbienteCacheData toData(final EstadoAmbienteEntity entity) {
        return new EstadoAmbienteCacheData(entity.getId(), entity.getNombre());
    }

    public EstadoAmbienteEntity toEntity(final EstadoAmbienteCacheData data) {
        return EstadoAmbienteEntity.create(data.id(), data.nombre());
    }

    public record EstadoAmbienteCacheData(UUID id, String nombre) {
    }
}
