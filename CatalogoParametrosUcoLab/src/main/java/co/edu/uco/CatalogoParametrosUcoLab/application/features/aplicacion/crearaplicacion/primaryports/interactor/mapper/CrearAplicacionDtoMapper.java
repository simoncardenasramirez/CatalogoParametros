package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDto;
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

    public CrearAplicacionDomain toDomain(final CrearAplicacionDto dto) {
        validateStringFields(dto);

        final var idOrganizacion = parseUUID(dto.getIdOrganizacion(),
                "El identificador de la organizacion no es valido.");

        final var activa = parseBoolean(dto.getActiva(),
                "El estado activo debe ser 'true' o 'false'.");

        final var fechaInicio = parseDateTime(dto.getFechaInicio(),
                "La fecha de inicio no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");

        final var fechaFinal = parseDateTime(dto.getFechaFinal(),
                "La fecha final no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");

        return CrearAplicacionDomain.create(
                UUID.randomUUID(),
                dto.getNombre(),
                idOrganizacion,
                activa,
                fechaInicio,
                fechaFinal
        );
    }

    private void validateStringFields(final CrearAplicacionDto dto) {
        if (TextHelper.isBlank(dto.getNombre())) {
            throw new AplicacionException("El nombre de la aplicacion es obligatorio.");
        }
        if (dto.getNombre().length() < 3 || dto.getNombre().length() > 50) {
            throw new AplicacionException("El nombre debe tener entre 3 y 50 caracteres.");
        }
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
