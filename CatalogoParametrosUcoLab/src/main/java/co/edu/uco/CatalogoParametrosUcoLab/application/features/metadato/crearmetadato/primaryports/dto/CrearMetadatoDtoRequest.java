package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

public final class CrearMetadatoDtoRequest {
    private String idParametro = TextHelper.EMPTY;
    private String idTipoMetadato = TextHelper.EMPTY;
    private String idAmbiente = TextHelper.EMPTY;
    private JsonNode valor = JsonNodeFactory.instance.nullNode();
    public CrearMetadatoDtoRequest() { }
    public CrearMetadatoDtoRequest(final String p, final String t, final String a, final JsonNode v) {
        setIdParametro(p); setIdTipoMetadato(t); setIdAmbiente(a); setValor(v);
    }
    public CrearMetadatoDtoRequest(final String p, final String t, final JsonNode v) {
        setIdParametro(p); setIdTipoMetadato(t); setValor(v);
    }
    public static CrearMetadatoDtoRequest create(final String p, final String t, final String a, final JsonNode v) { return new CrearMetadatoDtoRequest(p, t, a, v); }
    public static CrearMetadatoDtoRequest create(final String p, final String t, final JsonNode v) { return new CrearMetadatoDtoRequest(p, t, v); }
    public String getIdParametro() { return idParametro; }
    public void setIdParametro(final String value) { idParametro = TextHelper.applyTrim(value); ValidateHelper.validateId(idParametro, "identificador del parametro"); }
    public String getIdTipoMetadato() { return idTipoMetadato; }
    public void setIdTipoMetadato(final String value) { idTipoMetadato = TextHelper.applyTrim(value); ValidateHelper.validateId(idTipoMetadato, "identificador del tipo de metadato"); }
    public String getIdAmbiente() { return idAmbiente; }
    public void setIdAmbiente(final String value) { idAmbiente = TextHelper.applyTrim(value); ValidateHelper.validateId(idAmbiente, "identificador del ambiente"); }
    public JsonNode getValor() { return valor; }
    public void setValor(final JsonNode value) { valor = value == null ? JsonNodeFactory.instance.nullNode() : value.deepCopy(); }
}
