package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;

import java.util.UUID;

public final class CrearFuncionalidadDtoMapper {

    public static final CrearFuncionalidadDtoMapper INSTANCE = new CrearFuncionalidadDtoMapper();

    private CrearFuncionalidadDtoMapper() {
        super();
    }

    public CrearFuncionalidadDomain toDomain(final CrearFuncionalidadDto dto) {
        return CrearFuncionalidadDomain.create(
                UUID.randomUUID(),
                dto.getNombre(),
                dto.getIdModulo(),
                dto.isActivo(),
                dto.getFechaInicio(),
                dto.getFechaFinal()
        );
    }
}