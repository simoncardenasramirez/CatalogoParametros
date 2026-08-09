package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;

public final class CrearFuncionalidadDtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final CrearFuncionalidadDtoMapper INSTANCE = new CrearFuncionalidadDtoMapper();

    private CrearFuncionalidadDtoMapper() {
        super();
    }

    public CrearFuncionalidadDomain toDomain(final CrearFuncionalidadDtoRequest dto) {
        var dtoToMap = dto == null ? new CrearFuncionalidadDtoRequest() : dto;
        final var idModulo = UUID.fromString(dtoToMap.getIdModulo());
        final var activo = Boolean.parseBoolean(dtoToMap.getActivo());
        final var fechaInicio = LocalDateTime.parse(dtoToMap.getFechaInicio(), DATE_FORMATTER);
        final var fechaFinal = LocalDateTime.parse(dtoToMap.getFechaFinal(), DATE_FORMATTER);
        return CrearFuncionalidadDomain.create(
                UUID.randomUUID(),
                dtoToMap.getNombre(),
                idModulo,
                activo,
                fechaInicio,
                fechaFinal
        );
    }
}
