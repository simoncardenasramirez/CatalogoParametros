package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum TipoMetadatoCacheMapper {
    INSTANCE;

    public TipoMetadatoCacheData toData(final TipoMetadatoEntity entity) {
        return new TipoMetadatoCacheData(entity.getId(), entity.getTipo(), entity.getDetalle());
    }

    public TipoMetadatoEntity toEntity(final TipoMetadatoCacheData data) {
        return TipoMetadatoEntity.create(data.id(), data.tipo(), data.detalle());
    }

    public record TipoMetadatoCacheData(UUID id, String tipo, String detalle) {
    }
}
