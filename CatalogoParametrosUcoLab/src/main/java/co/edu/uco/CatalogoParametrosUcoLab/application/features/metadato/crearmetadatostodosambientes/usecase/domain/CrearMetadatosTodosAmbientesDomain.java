package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.domain;

import java.util.UUID;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

public final class CrearMetadatosTodosAmbientesDomain {
    private final UUID idParametro; private final UUID idTipoMetadato; private final JsonNode valor;
    private CrearMetadatosTodosAmbientesDomain(final UUID idParametro, final UUID idTipoMetadato, final JsonNode valor) {
        this.idParametro = idParametro; this.idTipoMetadato = idTipoMetadato;
        this.valor = valor == null ? JsonNodeFactory.instance.nullNode() : valor.deepCopy();
    }
    public static CrearMetadatosTodosAmbientesDomain create(final UUID idParametro, final UUID idTipoMetadato, final JsonNode valor) {
        return new CrearMetadatosTodosAmbientesDomain(idParametro, idTipoMetadato, valor);
    }
    public UUID getIdParametro() { return idParametro; }
    public UUID getIdTipoMetadato() { return idTipoMetadato; }
    public JsonNode getValor() { return valor; }
}
