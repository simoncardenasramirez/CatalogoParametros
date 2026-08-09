package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;

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
        final var idModulo = UUID.fromString(dtoToMap.getIdModulo());
        final var activo = Boolean.parseBoolean(dtoToMap.getActivo());
        final var fechaInicio = LocalDateTime.parse(dtoToMap.getFechaInicio(), DATE_FORMATTER);
        final var fechaFinal = LocalDateTime.parse(dtoToMap.getFechaFinal(), DATE_FORMATTER);
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
}
