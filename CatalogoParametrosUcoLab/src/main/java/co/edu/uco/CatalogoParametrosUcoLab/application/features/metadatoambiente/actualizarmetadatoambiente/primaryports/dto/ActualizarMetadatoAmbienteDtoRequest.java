package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.primaryports.dto.MetadatoAmbienteDtoRequest;

public final class ActualizarMetadatoAmbienteDtoRequest extends MetadatoAmbienteDtoRequest {

    public ActualizarMetadatoAmbienteDtoRequest() {
        super();
    }

    public ActualizarMetadatoAmbienteDtoRequest(final String idParametro, final String idAmbiente,
            final String idEstadoMetadatoAmbiente) {
        super(idParametro, idAmbiente, idEstadoMetadatoAmbiente);
    }

    public static ActualizarMetadatoAmbienteDtoRequest create(final String idParametro, final String idAmbiente,
            final String idEstadoMetadatoAmbiente) {
        return new ActualizarMetadatoAmbienteDtoRequest(idParametro, idAmbiente, idEstadoMetadatoAmbiente);
    }

}
