package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.exception.OrganizacionException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum ActualizarOrganizacionDtoMapper {
    INSTANCE;

    public ActualizarOrganizacionDomain toDomain(final ActualizarOrganizacionDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(dtoInput);
    }

    public ActualizarOrganizacionDtoInput toDtoInput(final ActualizarOrganizacionDtoRequest dto) {
        var dtoToMap = dto == null ? new ActualizarOrganizacionDtoRequest() : dto;
        final var id = parseUUID(dtoToMap.getId(), "El identificador de la organizacion no es valido.");
        return ActualizarOrganizacionDtoInput.create(id, dtoToMap.getNombre());
    }

    public ActualizarOrganizacionDomain toDomain(final ActualizarOrganizacionDtoInput dtoInput) {
        return ActualizarOrganizacionDomain.create(dtoInput.getId(), dtoInput.getNombre());
    }

    private UUID parseUUID(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            throw new OrganizacionException(errorMessage);
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new OrganizacionException(errorMessage + " Valor recibido: " + value);
        }
    }
}
