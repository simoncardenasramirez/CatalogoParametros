package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.consultarorganizacion.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.InteractorWithReturn;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;

public interface ConsultarOrganizacionInteractor extends InteractorWithReturn<UUID, List<OrganizacionEntity>> {

    List<OrganizacionEntity> execute();

    List<OrganizacionEntity> execute(int pagina, int tamanoPagina);
}