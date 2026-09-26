package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.interactor;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.dto.CrearAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

public interface CrearAmbienteInteractor {
    AmbienteEntity execute(CrearAmbienteDtoRequest request);
}
