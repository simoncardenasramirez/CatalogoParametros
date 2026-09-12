package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.mapper;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto.ActualizarModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto.ActualizarModuloDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;

public final class ActualizarModuloDtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static final ActualizarModuloDtoMapper INSTANCE = new ActualizarModuloDtoMapper();

    private ActualizarModuloDtoMapper() {
        super();
    }

    public ActualizarModuloDomain toDomain(final UUID id, final ActualizarModuloDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(id, dtoInput);
    }

    public ActualizarModuloDtoInput toDtoInput(final ActualizarModuloDtoRequest dto) {
        var dtoToMap = dto == null ? new ActualizarModuloDtoRequest() : dto;
        final var idAplicacion = UUID.fromString(dtoToMap.getIdAplicacion());
        final var activo = Boolean.parseBoolean(dtoToMap.getActivo());
        final var fechaInicio = OffsetDateTime.parse(dtoToMap.getFechaInicio(), DATE_FORMATTER);
        final var fechaFinal = OffsetDateTime.parse(dtoToMap.getFechaFinal(), DATE_FORMATTER);
        return ActualizarModuloDtoInput.create(
                dtoToMap.getNombre(),
                idAplicacion,
                activo,
                fechaInicio,
                fechaFinal
        );
    }

    public ActualizarModuloDomain toDomain(final UUID id, final ActualizarModuloDtoInput dtoInput) {
        return ActualizarModuloDomain.create(id, dtoInput.getNombre(), dtoInput.getIdAplicacion(), dtoInput.isActivo(),
                dtoInput.getFechaInicio(), dtoInput.getFechaFinal());
    }
}
