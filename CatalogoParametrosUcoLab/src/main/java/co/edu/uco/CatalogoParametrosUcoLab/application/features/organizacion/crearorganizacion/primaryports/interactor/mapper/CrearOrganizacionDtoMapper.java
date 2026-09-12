package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.mapper;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;

public final class CrearOrganizacionDtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

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
        final var fechaInicio = parseFecha(dtoToMap.getFechaInicio());
        final var fechaFinal = parseFecha(dtoToMap.getFechaFinal());
        return CrearOrganizacionDtoInput.create(nombre, fechaInicio, fechaFinal);
    }

    public CrearOrganizacionDomain toDomain(final CrearOrganizacionDtoInput dtoInput) {
        return CrearOrganizacionDomain.create(
                UUID.randomUUID(),
                dtoInput.getNombre(), dtoInput.getFechaInicio(), dtoInput.getFechaFinal()
        );
    }

    private OffsetDateTime parseFecha(final String fecha) {
        return TextHelper.isBlank(fecha)
                ? null : OffsetDateTime.parse(fecha, DATE_FORMATTER);
    }
}
