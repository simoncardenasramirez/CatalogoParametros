package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.exception.OrganizacionException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearOrganizacionDtoMapper {

    public static final CrearOrganizacionDtoMapper INSTANCE = new CrearOrganizacionDtoMapper();

    private CrearOrganizacionDtoMapper() {
        super();
    }

    public CrearOrganizacionDomain toDomain(final CrearOrganizacionDto dto) {
        var dtoToMap = dto == null ? new CrearOrganizacionDto() : dto;
        validateStringFields(dtoToMap);
        return CrearOrganizacionDomain.create(
                UUID.randomUUID(),
                dtoToMap.getNombre()
        );
    }

    private void validateStringFields(final CrearOrganizacionDto dto) {
        if (TextHelper.isBlank(dto.getNombre())) {
            throw new OrganizacionException("El nombre de la organizacion es obligatorio.");
        }
        if (dto.getNombre().length() < 3 || dto.getNombre().length() > 50) {
            throw new OrganizacionException("El nombre debe tener entre 3 y 50 caracteres.");
        }
    }
}
