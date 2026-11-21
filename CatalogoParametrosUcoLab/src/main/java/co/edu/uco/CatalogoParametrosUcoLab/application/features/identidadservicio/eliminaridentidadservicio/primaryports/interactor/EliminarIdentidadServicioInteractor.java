package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.eliminaridentidadservicio.primaryports.interactor;

import java.util.UUID;

public interface EliminarIdentidadServicioInteractor {
    void execute(UUID idAplicacion, String clientId);
}
