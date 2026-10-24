package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.interactor;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.dto.AdministrarCatalogoAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CatalogoAplicacionEntity;

public interface AdministrarCatalogoAplicacionInteractor {
    CatalogoAplicacionEntity consultar(UUID aplicacion);
    void crear(UUID aplicacion, String recurso, AdministrarCatalogoAplicacionDtoRequest datos);
    void actualizar(UUID aplicacion, String recurso, UUID id, AdministrarCatalogoAplicacionDtoRequest datos);
    void eliminar(UUID aplicacion, String recurso, UUID id);
}
