package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.dto;

import java.util.UUID;
import tools.jackson.databind.JsonNode;

public final class CrearMetadatosTodosAmbientesDtoInput {
    private final UUID idParametro; private final UUID idTipoMetadato; private final JsonNode valor;
    private CrearMetadatosTodosAmbientesDtoInput(final UUID idParametro, final UUID idTipoMetadato, final JsonNode valor) {
        this.idParametro = idParametro; this.idTipoMetadato = idTipoMetadato; this.valor = valor == null ? null : valor.deepCopy();
    }
    public static CrearMetadatosTodosAmbientesDtoInput create(final UUID idParametro, final UUID idTipoMetadato, final JsonNode valor) {
        return new CrearMetadatosTodosAmbientesDtoInput(idParametro, idTipoMetadato, valor);
    }
    public UUID getIdParametro() { return idParametro; }
    public UUID getIdTipoMetadato() { return idTipoMetadato; }
    public JsonNode getValor() { return valor; }
}
