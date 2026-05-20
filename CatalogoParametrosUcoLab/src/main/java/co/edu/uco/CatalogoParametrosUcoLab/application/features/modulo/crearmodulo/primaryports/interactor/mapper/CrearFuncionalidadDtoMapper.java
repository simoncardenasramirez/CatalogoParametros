package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.mapper;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.FuncionalidadDomain;

import java.util.UUID;

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