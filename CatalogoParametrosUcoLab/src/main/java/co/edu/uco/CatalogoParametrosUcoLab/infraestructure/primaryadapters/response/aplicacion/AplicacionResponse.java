package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.aplicacion;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class AplicacionResponse extends Response {

    private final List<AplicacionEntity> aplicaciones = new ArrayList<>();

    public List<AplicacionEntity> getAplicaciones() {
        return aplicaciones;
    }
}
