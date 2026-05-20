package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.funcionalidad;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.InteractorWithReturn;

public interface ConsultarFuncionalidadInteractor extends InteractorWithReturn<UUID, List<FuncionalidadEntity>> {
    
    List<FuncionalidadEntity> execute();
}
