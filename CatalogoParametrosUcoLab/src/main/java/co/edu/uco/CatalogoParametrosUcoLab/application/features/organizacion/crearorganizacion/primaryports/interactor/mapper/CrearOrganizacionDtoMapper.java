package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;

public final class CrearOrganizacionDtoMapper {

    public static final CrearOrganizacionDtoMapper INSTANCE = new CrearOrganizacionDtoMapper();

    private CrearOrganizacionDtoMapper() {
        super();
    }

    public CrearOrganizacionDomain toDomain(final CrearOrganizacionDto dto) {
        return CrearOrganizacionDomain.create(
                UUID.randomUUID(),
                dto.getNombre()
        );
    }
}
