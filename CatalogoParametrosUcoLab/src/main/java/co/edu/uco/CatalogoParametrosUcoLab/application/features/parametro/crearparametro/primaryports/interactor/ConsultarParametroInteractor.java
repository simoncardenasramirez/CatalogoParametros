package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.InteractorWithReturn;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

import java.util.List;
import java.util.UUID;

public interface ConsultarParametroInteractor extends InteractorWithReturn<UUID, List<ParametroEntity>> {
    
    List<ParametroEntity> execute();
}