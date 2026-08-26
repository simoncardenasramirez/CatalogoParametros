package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto;

import java.util.UUID;
public record ActualizarMetadatoDtoInput(UUID idParametro, UUID idTipoMetadato, String valor) { }
