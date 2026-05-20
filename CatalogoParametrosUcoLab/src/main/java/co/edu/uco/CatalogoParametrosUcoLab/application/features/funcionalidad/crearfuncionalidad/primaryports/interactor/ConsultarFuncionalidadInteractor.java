package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.InteractorWithReturn;

public interface ConsultarFuncionalidadInteractor extends InteractorWithReturn<UUID, List<FuncionalidadEntity>> {
    
    List<FuncionalidadEntity> execute();
}
