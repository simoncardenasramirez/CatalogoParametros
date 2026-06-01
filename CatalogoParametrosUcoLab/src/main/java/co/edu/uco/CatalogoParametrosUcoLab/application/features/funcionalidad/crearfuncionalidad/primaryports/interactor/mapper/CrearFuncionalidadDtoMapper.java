package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;

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