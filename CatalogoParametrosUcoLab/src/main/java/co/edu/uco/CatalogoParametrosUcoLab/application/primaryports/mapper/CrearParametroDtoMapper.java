package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.mapper;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.parametro.CrearParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;

public enum CrearParametroDtoMapper {
    INSTANCE;

    public ParametroDomain toDomain(final CrearParametroDto dto) {
        var dtoToMap = dto == null ? new CrearParametroDto() : dto;
        return ParametroDomain.create(UUIDHelper.getDefault(), dtoToMap.getNombre(), dtoToMap.getIdFuncionalidad(),
                dtoToMap.getIdTipoParametro(), dtoToMap.isActivo());
    }
}
