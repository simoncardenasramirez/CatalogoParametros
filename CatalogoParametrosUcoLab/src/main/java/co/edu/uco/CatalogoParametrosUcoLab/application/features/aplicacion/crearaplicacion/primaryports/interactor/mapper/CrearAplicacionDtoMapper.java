package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;

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
        final var idOrganizacion = UUID.fromString(dto.getIdOrganizacion());
        final var activa = Boolean.parseBoolean(dto.getActiva());
        final var fechaInicio = LocalDateTime.parse(dto.getFechaInicio(), DATE_FORMATTER);
        final var fechaFinal = LocalDateTime.parse(dto.getFechaFinal(), DATE_FORMATTER);

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
}
