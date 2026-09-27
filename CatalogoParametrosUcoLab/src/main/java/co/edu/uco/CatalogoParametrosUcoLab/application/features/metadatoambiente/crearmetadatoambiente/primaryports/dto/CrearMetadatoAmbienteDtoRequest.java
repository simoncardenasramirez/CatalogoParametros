package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

public final class CrearMetadatoAmbienteDtoRequest {
    private String idParametro;
    private String idAmbiente;
    private String idEstadoMetadatoAmbiente;

    public CrearMetadatoAmbienteDtoRequest() {
        this.idParametro = TextHelper.EMPTY;
        this.idAmbiente = TextHelper.EMPTY;
        this.idEstadoMetadatoAmbiente = TextHelper.EMPTY;
    }

    public CrearMetadatoAmbienteDtoRequest(final String idParametro, final String idAmbiente,
            final String idEstadoMetadatoAmbiente) {
        setIdParametro(idParametro);
        setIdAmbiente(idAmbiente);
        setIdEstadoMetadatoAmbiente(idEstadoMetadatoAmbiente);
    }

    public static CrearMetadatoAmbienteDtoRequest create(final String idParametro, final String idAmbiente,
            final String idEstadoMetadatoAmbiente) {
        return new CrearMetadatoAmbienteDtoRequest(idParametro, idAmbiente, idEstadoMetadatoAmbiente);
    }

    public String getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(final String value) {
        idParametro = TextHelper.applyTrim(value);
        ValidateHelper.validateId(idParametro, "identificador del parametro");
    }

    public String getIdAmbiente() {
        return idAmbiente;
    }

    public void setIdAmbiente(final String value) {
        idAmbiente = TextHelper.applyTrim(value);
        ValidateHelper.validateId(idAmbiente, "identificador del ambiente");
    }

    public String getIdEstadoMetadatoAmbiente() {
        return idEstadoMetadatoAmbiente;
    }

    public void setIdEstadoMetadatoAmbiente(final String value) {
        idEstadoMetadatoAmbiente = TextHelper.applyTrim(value);
        ValidateHelper.validateId(idEstadoMetadatoAmbiente,
                "identificador del estado de metadato ambiente");
    }
}
