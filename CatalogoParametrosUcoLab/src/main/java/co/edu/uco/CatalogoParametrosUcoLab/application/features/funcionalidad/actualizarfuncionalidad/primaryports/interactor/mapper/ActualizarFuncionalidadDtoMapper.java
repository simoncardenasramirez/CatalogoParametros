package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum ActualizarFuncionalidadDtoMapper {
    INSTANCE;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ActualizarFuncionalidadDtoMapper() {
    }

    public ActualizarFuncionalidadDomain toDomain(final UUID id, final ActualizarFuncionalidadDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(id, dtoInput);
    }

    public ActualizarFuncionalidadDtoInput toDtoInput(final ActualizarFuncionalidadDtoRequest dto) {
        var dtoToMap = dto == null ? new ActualizarFuncionalidadDtoRequest() : dto;
        final var idModulo = parseUUID(dtoToMap.getIdModulo(), "El identificador del modulo no es valido.");
        final var activo = parseBoolean(dtoToMap.getActivo(), "El estado activo debe ser 'true' o 'false'.");
        final var fechaInicio = parseDateTime(dtoToMap.getFechaInicio(), "La fecha de inicio no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");
        final var fechaFinal = parseDateTime(dtoToMap.getFechaFinal(), "La fecha final no tiene un formato valido (yyyy-MM-dd HH:mm:ss).");
        return ActualizarFuncionalidadDtoInput.create(
                dtoToMap.getNombre(),
                idModulo,
                activo,
                fechaInicio,
                fechaFinal
        );
    }

    public ActualizarFuncionalidadDomain toDomain(final UUID id, final ActualizarFuncionalidadDtoInput dtoInput) {
        return ActualizarFuncionalidadDomain.create(id, dtoInput.getNombre(), dtoInput.getIdModulo(), dtoInput.isActivo(),
                dtoInput.getFechaInicio(), dtoInput.getFechaFinal());
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
