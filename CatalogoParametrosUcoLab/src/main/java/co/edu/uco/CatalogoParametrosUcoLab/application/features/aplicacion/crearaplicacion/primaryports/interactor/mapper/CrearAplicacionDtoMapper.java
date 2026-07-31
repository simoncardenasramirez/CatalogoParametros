package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;

public final class CrearAplicacionDtoMapper {

    public static final CrearAplicacionDtoMapper INSTANCE = new CrearAplicacionDtoMapper();

    private CrearAplicacionDtoMapper() {
        super();
    }

    public CrearAplicacionDomain toDomain(final CrearAplicacionDto dto) {
        return CrearAplicacionDomain.create(
                UUID.randomUUID(),
                dto.getNombre(),
                dto.getIdOrganizacion(),
                dto.isActiva(),
                dto.getFechaInicio(),
                dto.getFechaFinal()
        );
    }
}
