package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto;

import java.util.UUID;
import tools.jackson.databind.JsonNode;
public record ActualizarMetadatoDtoInput(UUID idParametro, UUID idTipoMetadato, JsonNode valor) { }
