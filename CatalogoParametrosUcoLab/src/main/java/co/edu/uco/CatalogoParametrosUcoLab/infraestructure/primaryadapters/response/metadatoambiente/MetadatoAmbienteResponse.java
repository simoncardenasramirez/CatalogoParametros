package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.metadatoambiente;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class MetadatoAmbienteResponse extends Response {
    private final List<MetadatoAmbienteEntity> metadatosAmbiente = new ArrayList<>();

    public List<MetadatoAmbienteEntity> getMetadatosAmbiente() {
        return metadatosAmbiente;
    }
}
