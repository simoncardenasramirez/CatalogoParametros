package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.interactor.mapper;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto.ActualizarAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto.ActualizarAplicacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;

public final class ActualizarAplicacionDtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static final ActualizarAplicacionDtoMapper INSTANCE = new ActualizarAplicacionDtoMapper();

    private ActualizarAplicacionDtoMapper() {
        super();
    }

    public ActualizarAplicacionDomain toDomain(final UUID id, final ActualizarAplicacionDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(id, dtoInput);
    }

    public ActualizarAplicacionDtoInput toDtoInput(final ActualizarAplicacionDtoRequest dto) {
        var dtoToMap = dto == null ? new ActualizarAplicacionDtoRequest() : dto;
        final var idOrganizacion = UUID.fromString(dtoToMap.getIdOrganizacion());
        final var activa = Boolean.parseBoolean(dtoToMap.getActiva());
        final var fechaInicio = OffsetDateTime.parse(dtoToMap.getFechaInicio(), DATE_FORMATTER);
        final var fechaFinal = OffsetDateTime.parse(dtoToMap.getFechaFinal(), DATE_FORMATTER);
        return ActualizarAplicacionDtoInput.create(
                dtoToMap.getNombre(),
                idOrganizacion,
                activa,
                fechaInicio,
                fechaFinal
        );
    }

    public ActualizarAplicacionDomain toDomain(final UUID id, final ActualizarAplicacionDtoInput dtoInput) {
        return ActualizarAplicacionDomain.create(id, dtoInput.getNombre(), dtoInput.getIdOrganizacion(), dtoInput.isActiva(),
                dtoInput.getFechaInicio(), dtoInput.getFechaFinal());
    }
}
