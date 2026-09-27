package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.interactor;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.dto.ActualizarAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

public interface ActualizarAmbienteInteractor {
    AmbienteEntity execute(UUID id, ActualizarAmbienteDtoRequest request);
}
