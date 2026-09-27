package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.interactor;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.dto.ActualizarEstadoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

public interface ActualizarEstadoAmbienteInteractor {
    EstadoAmbienteEntity execute(UUID id, ActualizarEstadoAmbienteDtoRequest request);
}
