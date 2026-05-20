package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.funcionalidad.CrearFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.FuncionalidadDomain;

public final class CrearFuncionalidadDtoMapper {

    public static final CrearFuncionalidadDtoMapper INSTANCE = new CrearFuncionalidadDtoMapper();

    private CrearFuncionalidadDtoMapper() {
        super();
    }

    public FuncionalidadDomain toDomain(final CrearFuncionalidadDto dto) {
        return FuncionalidadDomain.create(
                UUID.randomUUID(),
                dto.getNombre(),
                dto.getIdModulo(),
                dto.isActivo(),
                dto.getFechaInicio(),
                dto.getFechaFinal()
        );
    }
}