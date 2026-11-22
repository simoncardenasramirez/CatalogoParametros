package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.identidadservicio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class IdentidadServicioResponse extends Response {
    private final List<IdentidadServicioEntity> identidadesServicio = new ArrayList<>();
    public List<IdentidadServicioEntity> getIdentidadesServicio() { return identidadesServicio; }
}
