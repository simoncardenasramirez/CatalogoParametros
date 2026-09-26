package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.interactor;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto.CrearMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

public interface CrearMetadatoAmbienteInteractor {
    MetadatoAmbienteEntity execute(CrearMetadatoAmbienteDtoRequest request);
}
