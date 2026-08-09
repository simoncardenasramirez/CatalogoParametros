package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.exception.AplicacionException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearAplicacionDtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final CrearAplicacionDtoMapper INSTANCE = new CrearAplicacionDtoMapper();

    private CrearAplicacionDtoMapper() {
        super();
    }

    public CrearAplicacionDomain toDomain(final CrearAplicacionDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(dtoInput);
    }

    public CrearAplicacionDtoInput toDtoInput(final CrearAplicacionDtoRequest dto) {
        final var idOrganizacion = parseUUID(dto.getIdOrganizacion(),
                "El identificador de la organizacion no es valido.");

        final var activa = parseBoolean(dto.getActiva(),
                "El estado activo debe ser 'true' o 'false'.");

        final var fechaInicio = parseDateTime(dto.getFechaInicio(),
                "La fecha de inicio no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");

        final var fechaFinal = parseDateTime(dto.getFechaFinal(),
                "La fecha final no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");

        return CrearAplicacionDtoInput.create(
                dto.getNombre(),
                idOrganizacion,
                activa,
                fechaInicio,
                fechaFinal
        );
    }

    public CrearAplicacionDomain toDomain(final CrearAplicacionDtoInput dtoInput) {
        return CrearAplicacionDomain.create(
                UUID.randomUUID(),
                dtoInput.getNombre(),
                dtoInput.getIdOrganizacion(),
                dtoInput.isActiva(),
                dtoInput.getFechaInicio(),
                dtoInput.getFechaFinal()
        );
    }

    private UUID parseUUID(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            throw new AplicacionException(errorMessage);
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new AplicacionException(errorMessage + " Valor recibido: " + value);
        }
    }

    private boolean parseBoolean(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            throw new AplicacionException(errorMessage);
        }
        if (!"true".equals(value) && !"false".equals(value)) {
            throw new AplicacionException(errorMessage + " Valor recibido: " + value);
        }
        return Boolean.parseBoolean(value);
    }

    private LocalDateTime parseDateTime(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new AplicacionException(errorMessage + " Valor recibido: " + value);
        }
    }
}
