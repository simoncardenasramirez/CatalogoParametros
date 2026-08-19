package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;

public enum ActualizarOrganizacionDtoMapper {
    INSTANCE;

    public ActualizarOrganizacionDomain toDomain(final UUID id, final ActualizarOrganizacionDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(id, dtoInput);
    }

    public ActualizarOrganizacionDtoInput toDtoInput(final ActualizarOrganizacionDtoRequest dto) {
        var dtoToMap = dto == null ? new ActualizarOrganizacionDtoRequest() : dto;
        return ActualizarOrganizacionDtoInput.create(dtoToMap.getNombre());
    }

    public ActualizarOrganizacionDomain toDomain(final UUID id, final ActualizarOrganizacionDtoInput dtoInput) {
        return ActualizarOrganizacionDomain.create(id, dtoInput.getNombre());
    }
}
