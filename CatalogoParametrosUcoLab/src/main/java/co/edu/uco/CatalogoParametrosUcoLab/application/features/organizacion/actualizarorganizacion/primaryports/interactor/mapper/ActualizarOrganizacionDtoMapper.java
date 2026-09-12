package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.mapper;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;

public enum ActualizarOrganizacionDtoMapper {
    INSTANCE;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public ActualizarOrganizacionDomain toDomain(final UUID id, final ActualizarOrganizacionDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(id, dtoInput);
    }

    public ActualizarOrganizacionDtoInput toDtoInput(final ActualizarOrganizacionDtoRequest dto) {
        var dtoToMap = dto == null ? new ActualizarOrganizacionDtoRequest() : dto;
        final var fechaInicio = parseFecha(dtoToMap.getFechaInicio());
        final var fechaFinal = parseFecha(dtoToMap.getFechaFinal());
        return ActualizarOrganizacionDtoInput.create(dtoToMap.getNombre(), fechaInicio, fechaFinal);
    }

    public ActualizarOrganizacionDomain toDomain(final UUID id, final ActualizarOrganizacionDtoInput dtoInput) {
        return ActualizarOrganizacionDomain.create(id, dtoInput.getNombre(), dtoInput.getFechaInicio(), dtoInput.getFechaFinal());
    }

    private OffsetDateTime parseFecha(final String fecha) {
        return TextHelper.isBlank(fecha)
                ? null : OffsetDateTime.parse(fecha, DATE_FORMATTER);
    }
}
