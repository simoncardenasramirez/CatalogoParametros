package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.consultarmodulo.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.InteractorWithReturn;

public interface ConsultarModuloInteractor extends InteractorWithReturn<UUID, List<ModuloEntity>> {
    
    List<ModuloEntity> execute();

    List<ModuloEntity> execute(int pagina, int tamanoPagina);
}
