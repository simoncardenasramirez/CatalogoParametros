package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;
import tools.jackson.databind.JsonNode;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum MetadatoCacheMapper {
    INSTANCE;

    public MetadatoCacheData toData(final MetadatoEntity entity) {
        return new MetadatoCacheData(entity.getId(), entity.getIdParametro(), entity.getIdTipoMetadato(), entity.getIdAmbiente(), entity.getValor());
    }

    public MetadatoEntity toEntity(final MetadatoCacheData data) {
        return MetadatoEntity.create(data.id(), data.idParametro(), data.idTipoMetadato(), data.idAmbiente(), data.valor());
    }

    public record MetadatoCacheData(UUID id, UUID idParametro, UUID idTipoMetadato, UUID idAmbiente, JsonNode valor) {
        public MetadatoCacheData(UUID id, UUID p, UUID t, JsonNode v) { this(id, p, t, null, v); }
    }
}
