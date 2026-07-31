package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.organizacion;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class OrganizacionResponse extends Response {

    private final List<OrganizacionEntity> organizaciones = new ArrayList<>();

    public List<OrganizacionEntity> getOrganizaciones() {
        return organizaciones;
    }
}
