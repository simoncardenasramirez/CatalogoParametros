package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;



import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CrearModuloDtoMapper {

    public static final CrearModuloDtoMapper INSTANCE = new CrearModuloDtoMapper();

    private CrearModuloDtoMapper() {
        super();
    }

    public CrearModuloDomain toDomain(final CrearModuloDto dto) {
        var dtoToMap = dto == null ? new CrearModuloDto() : dto;
        return CrearModuloDomain.create(
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
