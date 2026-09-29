package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.ambiente;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class AmbienteResponse extends Response {
    private final List<AmbienteEntity> ambientes = new ArrayList<>();

    public List<AmbienteEntity> getAmbientes() {
        return ambientes;
    }
}
