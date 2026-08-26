package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

public final class CrearMetadatoDtoRequest {
    private String idParametro;
    private String idTipoMetadato;
    private String valor;

    public CrearMetadatoDtoRequest() { this(TextHelper.EMPTY, TextHelper.EMPTY, TextHelper.EMPTY); }
    public CrearMetadatoDtoRequest(final String idParametro, final String idTipoMetadato, final String valor) {
        setIdParametro(idParametro);
        setIdTipoMetadato(idTipoMetadato);
        setValor(valor);
    }
    public static CrearMetadatoDtoRequest create(final String idParametro, final String idTipoMetadato, final String valor) {
        return new CrearMetadatoDtoRequest(idParametro, idTipoMetadato, valor);
    }
    public String getIdParametro() { return idParametro; }
    public void setIdParametro(final String value) {
        idParametro = TextHelper.applyTrim(value);
        ValidateHelper.validateId(idParametro, "identificador del parametro");
    }
    public String getIdTipoMetadato() { return idTipoMetadato; }
    public void setIdTipoMetadato(final String value) {
        idTipoMetadato = TextHelper.applyTrim(value);
        ValidateHelper.validateId(idTipoMetadato, "identificador del tipo de metadato");
    }
    public String getValor() { return valor; }
    public void setValor(final String valor) { this.valor = TextHelper.applyTrim(valor); }
}
