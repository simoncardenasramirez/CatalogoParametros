package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadotoken.consultarestadotoken.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;

public interface ConsultarEstadoTokenInteractor {
    List<EstadoTokenEntity> execute();
    List<EstadoTokenEntity> execute(UUID id);
}
