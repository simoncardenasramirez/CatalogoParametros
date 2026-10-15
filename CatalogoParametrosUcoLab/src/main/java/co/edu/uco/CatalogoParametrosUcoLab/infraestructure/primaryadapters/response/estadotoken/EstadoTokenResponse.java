package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.estadotoken;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class EstadoTokenResponse extends Response {
    private final List<EstadoTokenEntity> estadosToken = new ArrayList<>();
    public List<EstadoTokenEntity> getEstadosToken() { return estadosToken; }
}
