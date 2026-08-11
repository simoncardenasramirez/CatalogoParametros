package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.interactor;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto.ActualizarAplicacionDtoRequest;

public interface ActualizarAplicacionInteractor {

    void execute(UUID id, ActualizarAplicacionDtoRequest data);
}
