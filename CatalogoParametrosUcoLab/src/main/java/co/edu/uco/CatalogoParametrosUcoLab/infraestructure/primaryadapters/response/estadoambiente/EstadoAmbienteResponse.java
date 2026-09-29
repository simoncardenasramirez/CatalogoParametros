package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.estadoambiente;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class EstadoAmbienteResponse extends Response {
    private final List<EstadoAmbienteEntity> estadosAmbiente = new ArrayList<>();

    public List<EstadoAmbienteEntity> getEstadosAmbiente() {
        return estadosAmbiente;
    }
}
