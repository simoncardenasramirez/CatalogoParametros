package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.consultaridentidadservicio.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;

public interface ConsultarIdentidadServicioInteractor {
    List<IdentidadServicioEntity> execute(UUID idAplicacion);
}
