package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.exception.OrganizacionException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum ActualizarOrganizacionDtoMapper {
    INSTANCE;

    public ActualizarOrganizacionDomain toDomain(final ActualizarOrganizacionDto dto) {
        var dtoToMap = dto == null ? new ActualizarOrganizacionDto() : dto;
        validateStringFields(dtoToMap);
        final var id = parseUUID(dtoToMap.getId(), "El identificador de la organizacion no es valido.");
        return ActualizarOrganizacionDomain.create(id, dtoToMap.getNombre());
    }

    private void validateStringFields(final ActualizarOrganizacionDto dto) {
        if (TextHelper.isBlank(dto.getNombre())) {
            throw new OrganizacionException("El nombre de la organizacion es obligatorio.");
        }
        if (dto.getNombre().length() < 3 || dto.getNombre().length() > 50) {
            throw new OrganizacionException("El nombre debe tener entre 3 y 50 caracteres.");
        }
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
