package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;


import java.time.OffsetDateTime;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;

public final class CrearFuncionalidadDtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static final CrearFuncionalidadDtoMapper INSTANCE = new CrearFuncionalidadDtoMapper();

    private CrearFuncionalidadDtoMapper() {
        super();
    }

    public CrearFuncionalidadDomain toDomain(final CrearFuncionalidadDtoRequest dto) {
        var dtoToMap = dto == null ? new CrearFuncionalidadDtoRequest() : dto;
        final var idModulo = UUID.fromString(dtoToMap.getIdModulo());
        final var activo = Boolean.parseBoolean(dtoToMap.getActivo());
        final var fechaInicio = OffsetDateTime.parse(dtoToMap.getFechaInicio(), DATE_FORMATTER);
        final var fechaFinal = OffsetDateTime.parse(dtoToMap.getFechaFinal(), DATE_FORMATTER);
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
