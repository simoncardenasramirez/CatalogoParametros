package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;

public final class CrearModuloDtoMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final CrearModuloDtoMapper INSTANCE = new CrearModuloDtoMapper();

    private CrearModuloDtoMapper() {
        super();
    }

    public CrearModuloDomain toDomain(final CrearModuloDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(dtoInput);
    }

    public CrearModuloDtoInput toDtoInput(final CrearModuloDtoRequest dto) {
        var dtoToMap = dto == null ? new CrearModuloDtoRequest() : dto;
        final var idAplicacion = UUID.fromString(dtoToMap.getIdAplicacion());
        final var activo = Boolean.parseBoolean(dtoToMap.getActivo());
        final var fechaInicio = LocalDateTime.parse(dtoToMap.getFechaInicio(), DATE_FORMATTER);
        final var fechaFinal = LocalDateTime.parse(dtoToMap.getFechaFinal(), DATE_FORMATTER);
        return CrearModuloDtoInput.create(
                dtoToMap.getNombre(),
                idAplicacion,
                activo,
                fechaInicio,
                fechaFinal
        );
    }

    public CrearModuloDomain toDomain(final CrearModuloDtoInput dtoInput) {
        return CrearModuloDomain.create(
                UUID.randomUUID(),
                dtoInput.getNombre(),
                dtoInput.getIdAplicacion(),
                dtoInput.isActivo(),
                dtoInput.getFechaInicio(),
                dtoInput.getFechaFinal()
        );
    }
}
