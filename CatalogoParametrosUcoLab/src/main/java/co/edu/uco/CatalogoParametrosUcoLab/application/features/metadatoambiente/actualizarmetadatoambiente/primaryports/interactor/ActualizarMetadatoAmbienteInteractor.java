package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.interactor;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.dto.ActualizarMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

public interface ActualizarMetadatoAmbienteInteractor {
    MetadatoAmbienteEntity execute(UUID id, ActualizarMetadatoAmbienteDtoRequest request);
}
