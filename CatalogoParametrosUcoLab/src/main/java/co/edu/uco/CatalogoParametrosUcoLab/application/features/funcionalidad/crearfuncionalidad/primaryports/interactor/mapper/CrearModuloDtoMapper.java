package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.exception.ModuloException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearModuloDtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final CrearModuloDtoMapper INSTANCE = new CrearModuloDtoMapper();

    private CrearModuloDtoMapper() {
        super();
    }

    public CrearModuloDomain toDomain(final CrearModuloDto dto) {
        var dtoToMap = dto == null ? new CrearModuloDto() : dto;
        validateStringFields(dtoToMap);
        final var idAplicacion = parseUUID(dtoToMap.getIdAplicacion(), "El identificador de la aplicacion no es valido.");
        final var activo = parseBoolean(dtoToMap.getActivo(), "El estado activo debe ser 'true' o 'false'.");
        final var fechaInicio = parseDateTime(dtoToMap.getFechaInicio(), "La fecha de inicio no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");
        final var fechaFinal = parseDateTime(dtoToMap.getFechaFinal(), "La fecha final no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");
        return CrearModuloDomain.create(
                UUID.randomUUID(),
                dtoToMap.getNombre(),
                idAplicacion,
                activo,
                fechaInicio,
                fechaFinal
        );
    }

    private void validateStringFields(final CrearModuloDto dto) {
        if (TextHelper.isBlank(dto.getNombre())) {
            throw new ModuloException("El nombre del modulo es obligatorio.");
        }
        if (dto.getNombre().length() < 3 || dto.getNombre().length() > 50) {
            throw new ModuloException("El nombre debe tener entre 3 y 50 caracteres.");
        }
    }

    private UUID parseUUID(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            throw new ModuloException(errorMessage);
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new ModuloException(errorMessage + " Valor recibido: " + value);
        }
    }

    private boolean parseBoolean(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            throw new ModuloException(errorMessage);
        }
        if (!"true".equals(value) && !"false".equals(value)) {
            throw new ModuloException(errorMessage + " Valor recibido: " + value);
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
            throw new ModuloException(errorMessage + " Valor recibido: " + value);
        }
    }
}
