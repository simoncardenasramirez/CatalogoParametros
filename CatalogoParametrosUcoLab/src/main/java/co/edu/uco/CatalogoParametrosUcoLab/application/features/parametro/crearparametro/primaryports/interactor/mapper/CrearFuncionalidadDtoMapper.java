package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearFuncionalidadDtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final CrearFuncionalidadDtoMapper INSTANCE = new CrearFuncionalidadDtoMapper();

    private CrearFuncionalidadDtoMapper() {
        super();
    }

    public CrearFuncionalidadDomain toDomain(final CrearFuncionalidadDto dto) {
        var dtoToMap = dto == null ? new CrearFuncionalidadDto() : dto;
        validateStringFields(dtoToMap);
        final var idModulo = parseUUID(dtoToMap.getIdModulo(), "El identificador del modulo no es valido.");
        final var activo = parseBoolean(dtoToMap.getActivo(), "El estado activo debe ser 'true' o 'false'.");
        final var fechaInicio = parseDateTime(dtoToMap.getFechaInicio(), "La fecha de inicio no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");
        final var fechaFinal = parseDateTime(dtoToMap.getFechaFinal(), "La fecha final no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");
        return CrearFuncionalidadDomain.create(
                UUID.randomUUID(),
                dtoToMap.getNombre(),
                idModulo,
                activo,
                fechaInicio,
                fechaFinal
        );
    }

    private void validateStringFields(final CrearFuncionalidadDto dto) {
        if (TextHelper.isBlank(dto.getNombre())) {
            throw new FuncionalidadException("El nombre de la funcionalidad es obligatorio.");
        }
        if (dto.getNombre().length() < 3 || dto.getNombre().length() > 50) {
            throw new FuncionalidadException("El nombre debe tener entre 3 y 50 caracteres.");
        }
    }

    private UUID parseUUID(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            throw new FuncionalidadException(errorMessage);
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new FuncionalidadException(errorMessage + " Valor recibido: " + value);
        }
    }

    private boolean parseBoolean(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            throw new FuncionalidadException(errorMessage);
        }
        if (!"true".equals(value) && !"false".equals(value)) {
            throw new FuncionalidadException(errorMessage + " Valor recibido: " + value);
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
            throw new FuncionalidadException(errorMessage + " Valor recibido: " + value);
        }
    }
}
