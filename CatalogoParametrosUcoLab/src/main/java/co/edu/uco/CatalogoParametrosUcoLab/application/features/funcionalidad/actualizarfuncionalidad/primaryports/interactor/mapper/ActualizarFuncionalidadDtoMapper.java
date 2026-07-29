package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;

public enum ActualizarFuncionalidadDtoMapper {
    INSTANCE;

    ActualizarFuncionalidadDtoMapper() {
    }

    public ActualizarFuncionalidadDomain toDomain(final UUID id, final ActualizarFuncionalidadDto dto) {
        var dtoToMap = dto == null ? new ActualizarFuncionalidadDto() : dto;
        return ActualizarFuncionalidadDomain.create(id, dtoToMap.getNombre(), dtoToMap.getIdModulo(),
                dtoToMap.isActivo(), dtoToMap.getFechaInicio(), dtoToMap.getFechaFinal());
    }
}