package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.consultaraplicacion.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.InteractorWithReturn;

public interface ConsultarAplicacionInteractor extends InteractorWithReturn<UUID, List<AplicacionEntity>> {
    
    List<AplicacionEntity> execute();
}
