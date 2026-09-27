package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.interactor;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.dto.CrearEstadoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

public interface CrearEstadoAmbienteInteractor {
    EstadoAmbienteEntity execute(CrearEstadoAmbienteDtoRequest request);
}
