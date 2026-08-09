package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;

public final class CrearOrganizacionDtoMapper {

    public static final CrearOrganizacionDtoMapper INSTANCE = new CrearOrganizacionDtoMapper();

    private CrearOrganizacionDtoMapper() {
        super();
    }

    public CrearOrganizacionDomain toDomain(final CrearOrganizacionDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(dtoInput);
    }

    public CrearOrganizacionDtoInput toDtoInput(final CrearOrganizacionDtoRequest dto) {
        var dtoToMap = dto == null ? new CrearOrganizacionDtoRequest() : dto;
        final var nombre = dtoToMap.getNombre();
        return CrearOrganizacionDtoInput.create(nombre);
    }

    public CrearOrganizacionDomain toDomain(final CrearOrganizacionDtoInput dtoInput) {
        return CrearOrganizacionDomain.create(
                UUID.randomUUID(),
                dtoInput.getNombre()
        );
    }
}
