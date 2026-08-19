package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoRequest;

public interface ActualizarOrganizacionInteractor {
    void execute(UUID id, ActualizarOrganizacionDtoRequest data);
}
