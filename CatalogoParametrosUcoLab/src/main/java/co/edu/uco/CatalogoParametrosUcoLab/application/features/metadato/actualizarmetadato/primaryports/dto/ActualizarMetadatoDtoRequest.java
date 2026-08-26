package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;
public final class ActualizarMetadatoDtoRequest {
    private String idParametro;
    private String idTipoMetadato;
    private String valor;
    public ActualizarMetadatoDtoRequest() { }
    public ActualizarMetadatoDtoRequest(final String idParametro, final String idTipoMetadato, final String valor) {
        setIdParametro(idParametro); setIdTipoMetadato(idTipoMetadato); setValor(valor);
    }
    public static ActualizarMetadatoDtoRequest create(final String p, final String t, final String v) { return new ActualizarMetadatoDtoRequest(p, t, v); }
    public String getIdParametro() { return idParametro; }
    public void setIdParametro(final String value) { idParametro = TextHelper.applyTrim(value); ValidateHelper.validateId(idParametro, "identificador del parametro"); }
    public String getIdTipoMetadato() { return idTipoMetadato; }
    public void setIdTipoMetadato(final String value) { idTipoMetadato = TextHelper.applyTrim(value); ValidateHelper.validateId(idTipoMetadato, "identificador del tipo de metadato"); }
    public String getValor() { return valor; }
    public void setValor(final String value) { valor = TextHelper.applyTrim(value); }
}
