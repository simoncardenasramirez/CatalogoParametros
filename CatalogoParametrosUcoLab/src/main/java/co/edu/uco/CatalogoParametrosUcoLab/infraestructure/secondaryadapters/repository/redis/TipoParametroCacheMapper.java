package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum TipoParametroCacheMapper {
    INSTANCE;

    public TipoParametroCacheData toData(final TipoParametroEntity entity) {
        return new TipoParametroCacheData(entity.getId(), entity.getNombre());
    }

    public TipoParametroEntity toEntity(final TipoParametroCacheData data) {
        return TipoParametroEntity.create(data.id(), data.nombre());
    }

    public record TipoParametroCacheData(UUID id, String nombre) {
    }
}
