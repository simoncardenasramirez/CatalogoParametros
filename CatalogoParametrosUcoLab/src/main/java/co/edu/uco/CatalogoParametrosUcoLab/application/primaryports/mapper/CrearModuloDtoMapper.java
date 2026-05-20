package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.mapper;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.modulo.CrearModuloDto;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.ModuloDomain;

public final class CrearModuloDtoMapper {

    public static final CrearModuloDtoMapper INSTANCE = new CrearModuloDtoMapper();

    private CrearModuloDtoMapper() {
        super();
    }

    public ModuloDomain toDomain(final CrearModuloDto dto) {
        var dtoToMap = dto == null ? new CrearModuloDto() : dto;
        return ModuloDomain.create(
                UUID.randomUUID(),
                dtoToMap.getNombre(),
                dtoToMap.getIdAplicacion(),
                dtoToMap.isActivo(),
                mapFecha(dtoToMap.getFechaInicio()),
                mapFecha(dtoToMap.getFechaFinal())
        );
    }

    private LocalDateTime mapFecha(final String fecha) {
        if (fecha == null || fecha.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(fecha);
    }
}
