package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

public final class CrearMetadatosTodosAmbientesDtoRequest {
    private String idParametro = TextHelper.EMPTY;
    private String idTipoMetadato = TextHelper.EMPTY;
    private JsonNode valor = JsonNodeFactory.instance.nullNode();
    public CrearMetadatosTodosAmbientesDtoRequest() { }
    public CrearMetadatosTodosAmbientesDtoRequest(final String idParametro, final String idTipoMetadato, final JsonNode valor) {
        setIdParametro(idParametro); setIdTipoMetadato(idTipoMetadato); setValor(valor);
    }
    public static CrearMetadatosTodosAmbientesDtoRequest create(final String idParametro, final String idTipoMetadato, final JsonNode valor) {
        return new CrearMetadatosTodosAmbientesDtoRequest(idParametro, idTipoMetadato, valor);
    }
    public String getIdParametro() { return idParametro; }
    public void setIdParametro(final String value) { idParametro = TextHelper.applyTrim(value); ValidateHelper.validateId(idParametro, "identificador del parametro"); }
    public String getIdTipoMetadato() { return idTipoMetadato; }
    public void setIdTipoMetadato(final String value) { idTipoMetadato = TextHelper.applyTrim(value); ValidateHelper.validateId(idTipoMetadato, "identificador del tipo de metadato"); }
    public JsonNode getValor() { return valor; }
    public void setValor(final JsonNode value) { valor = value == null ? JsonNodeFactory.instance.nullNode() : value.deepCopy(); }
}
