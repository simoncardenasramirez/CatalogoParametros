package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum MetadatoAmbienteCacheMapper {
    INSTANCE;

    public MetadatoAmbienteCacheData toData(final MetadatoAmbienteEntity entity) {
        return new MetadatoAmbienteCacheData(entity.getId(), entity.getIdParametro(), entity.getIdAmbiente(), entity.getIdEstadoMetadatoAmbiente());
    }

    public MetadatoAmbienteEntity toEntity(final MetadatoAmbienteCacheData data) {
        return MetadatoAmbienteEntity.create(data.id(), data.idParametro(), data.idAmbiente(), data.idEstadoMetadatoAmbiente());
    }

    public record MetadatoAmbienteCacheData(UUID id, UUID idParametro, UUID idAmbiente, UUID idEstadoMetadatoAmbiente) {
    }
}
