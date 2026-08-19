package co.edu.uco.CatalogoParametrosUcoLab.application.features.tipoparametro.consultartipoparametro.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.InteractorWithReturn;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;

public interface ConsultarTipoParametroInteractor extends InteractorWithReturn<UUID, List<TipoParametroEntity>> {

    List<TipoParametroEntity> execute();
}
