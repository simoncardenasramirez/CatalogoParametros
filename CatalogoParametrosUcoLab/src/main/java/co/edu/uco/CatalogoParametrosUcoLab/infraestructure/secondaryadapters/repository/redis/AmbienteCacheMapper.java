package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum AmbienteCacheMapper {
    INSTANCE;

    public AmbienteCacheData toData(final AmbienteEntity entity) {
        return new AmbienteCacheData(entity.getId(), entity.getNombre());
    }

    public AmbienteEntity toEntity(final AmbienteCacheData data) {
        return AmbienteEntity.create(data.id(), data.nombre());
    }

    public record AmbienteCacheData(UUID id, String nombre) {
    }
}
