package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.interactor;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto.RegistrarIdentidadServicioDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;

public interface RegistrarIdentidadServicioInteractor {
    IdentidadServicioEntity execute(UUID idAplicacion, RegistrarIdentidadServicioDtoRequest request);
}
