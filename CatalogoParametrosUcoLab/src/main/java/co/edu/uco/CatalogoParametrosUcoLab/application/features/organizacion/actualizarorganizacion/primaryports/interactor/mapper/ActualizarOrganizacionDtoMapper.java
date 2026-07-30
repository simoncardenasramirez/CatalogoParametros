package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.mapper;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;

public enum ActualizarOrganizacionDtoMapper {
    INSTANCE;

    public ActualizarOrganizacionDomain toDomain(final ActualizarOrganizacionDto dto) {
        return ActualizarOrganizacionDomain.create(dto.getId(), dto.getNombre());
    }
}
