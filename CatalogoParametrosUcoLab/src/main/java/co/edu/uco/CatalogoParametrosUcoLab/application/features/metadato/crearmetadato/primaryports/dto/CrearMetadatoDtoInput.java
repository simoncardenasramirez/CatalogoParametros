package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto;

import java.util.UUID;
import tools.jackson.databind.JsonNode;

public final class CrearMetadatoDtoInput {
    private final UUID idParametro;
    private final UUID idTipoMetadato;
    private final UUID idAmbiente;
    private final JsonNode valor;
    private CrearMetadatoDtoInput(final UUID idParametro, final UUID idTipoMetadato, final UUID idAmbiente, final JsonNode valor) {
        this.idParametro = idParametro;
        this.idTipoMetadato = idTipoMetadato;
        this.idAmbiente = idAmbiente;
        this.valor = valor;
    }
    public static CrearMetadatoDtoInput create(final UUID idParametro, final UUID idTipoMetadato, final UUID idAmbiente, final JsonNode valor) {
        return new CrearMetadatoDtoInput(idParametro, idTipoMetadato, idAmbiente, valor);
    }
    public UUID getIdParametro() { return idParametro; }
    public UUID getIdTipoMetadato() { return idTipoMetadato; }
    public static CrearMetadatoDtoInput create(final UUID p, final UUID t, final JsonNode v) { return create(p, t, null, v); }
    public UUID getIdAmbiente() { return idAmbiente; }
    public JsonNode getValor() { return valor; }
}
