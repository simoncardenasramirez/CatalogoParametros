package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.mapper;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearParametroDtoMapper {
    INSTANCE;

    public CrearParametroDomain toDomain(final CrearParametroDto dto) {
        var dtoToMap = dto == null ? new CrearParametroDto() : dto;
        return CrearParametroDomain.create(UUIDHelper.getDefault(), dtoToMap.getNombre(), dtoToMap.getIdFuncionalidad(),
                dtoToMap.getIdTipoParametro(), dtoToMap.isActivo());
    }
}
