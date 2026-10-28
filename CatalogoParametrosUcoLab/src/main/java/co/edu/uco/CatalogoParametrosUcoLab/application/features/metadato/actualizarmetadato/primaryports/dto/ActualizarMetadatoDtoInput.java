package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto;
import java.util.UUID;
import tools.jackson.databind.JsonNode;
public record ActualizarMetadatoDtoInput(UUID idParametro, UUID idTipoMetadato, UUID idAmbiente, JsonNode valor) {
    public ActualizarMetadatoDtoInput(UUID p, UUID t, JsonNode v) { this(p, t, null, v); }
}
