package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto.RegistrarIdentidadServicioDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;

public interface RegistrarIdentidadServicio {
    IdentidadServicioEntity execute(UUID idAplicacion, RegistrarIdentidadServicioDtoInput input);
}
