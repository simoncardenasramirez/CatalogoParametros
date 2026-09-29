package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.primaryports.dto.MetadatoAmbienteDtoRequest;

public final class CrearMetadatoAmbienteDtoRequest extends MetadatoAmbienteDtoRequest {

    public CrearMetadatoAmbienteDtoRequest() {
        super();
    }

    public CrearMetadatoAmbienteDtoRequest(final String idParametro, final String idAmbiente,
            final String idEstadoMetadatoAmbiente) {
        super(idParametro, idAmbiente, idEstadoMetadatoAmbiente);
    }

    public static CrearMetadatoAmbienteDtoRequest create(final String idParametro, final String idAmbiente,
            final String idEstadoMetadatoAmbiente) {
        return new CrearMetadatoAmbienteDtoRequest(idParametro, idAmbiente, idEstadoMetadatoAmbiente);
    }

}
