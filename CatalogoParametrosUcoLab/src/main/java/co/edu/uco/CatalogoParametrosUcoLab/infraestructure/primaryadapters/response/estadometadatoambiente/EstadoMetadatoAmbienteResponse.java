package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.estadometadatoambiente;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class EstadoMetadatoAmbienteResponse extends Response {
    private final List<EstadoMetadatoAmbienteEntity> estadosMetadatoAmbiente = new ArrayList<>();

    public List<EstadoMetadatoAmbienteEntity> getEstadosMetadatoAmbiente() {
        return estadosMetadatoAmbiente;
    }
}
