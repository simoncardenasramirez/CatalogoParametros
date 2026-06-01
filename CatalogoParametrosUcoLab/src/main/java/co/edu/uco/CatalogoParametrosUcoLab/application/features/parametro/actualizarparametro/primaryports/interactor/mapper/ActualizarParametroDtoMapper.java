package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;

public enum ActualizarParametroDtoMapper {
    INSTANCE;

    public ActualizarParametroDomain toDomain(final UUID id, final ActualizarParametroDto dto) {
        var dtoToMap = dto == null ? new ActualizarParametroDto() : dto;
        return ActualizarParametroDomain.create(id, dtoToMap.getNombre(), dtoToMap.getIdFuncionalidad(),
                dtoToMap.getIdTipoParametro(), dtoToMap.isActivo());
    }
}
