package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.interactor;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.dto.ActualizarEstadoMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

public interface ActualizarEstadoMetadatoAmbienteInteractor {
    EstadoMetadatoAmbienteEntity execute(UUID id, ActualizarEstadoMetadatoAmbienteDtoRequest request);
}
