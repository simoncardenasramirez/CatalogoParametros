package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearParametroDtoMapper {
    INSTANCE;

    public ParametroDomain toDomain(final CrearParametroDto dto) {
        var dtoToMap = dto == null ? new CrearParametroDto() : dto;
        return ParametroDomain.create(UUIDHelper.getDefault(), dtoToMap.getNombre(), dtoToMap.getIdFuncionalidad(),
                dtoToMap.getIdTipoParametro(), dtoToMap.isActivo());
    }
}
