package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.parametro;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.InteractorWithReturn;

public interface ConsultarParametroInteractor extends InteractorWithReturn<UUID, List<ParametroEntity>> {
    
    List<ParametroEntity> execute();
}