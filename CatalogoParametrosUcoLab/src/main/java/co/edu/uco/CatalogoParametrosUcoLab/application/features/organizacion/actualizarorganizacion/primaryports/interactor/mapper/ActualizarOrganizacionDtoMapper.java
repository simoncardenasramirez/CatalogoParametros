package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;

public enum ActualizarOrganizacionDtoMapper {
    INSTANCE;

    public ActualizarOrganizacionDomain toDomain(final ActualizarOrganizacionDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(dtoInput);
    }

    public ActualizarOrganizacionDtoInput toDtoInput(final ActualizarOrganizacionDtoRequest dto) {
        var dtoToMap = dto == null ? new ActualizarOrganizacionDtoRequest() : dto;
        final var id = UUID.fromString(dtoToMap.getId());
        return ActualizarOrganizacionDtoInput.create(id, dtoToMap.getNombre());
    }

    public ActualizarOrganizacionDomain toDomain(final ActualizarOrganizacionDtoInput dtoInput) {
        return ActualizarOrganizacionDomain.create(dtoInput.getId(), dtoInput.getNombre());
    }
}
