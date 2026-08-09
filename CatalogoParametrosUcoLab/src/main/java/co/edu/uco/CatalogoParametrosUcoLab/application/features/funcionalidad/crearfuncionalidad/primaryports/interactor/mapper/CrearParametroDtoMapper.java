package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearParametroDtoMapper {
    INSTANCE;

    public CrearParametroDomain toDomain(final CrearParametroDtoRequest dto) {
        var dtoToMap = dto == null ? new CrearParametroDtoRequest() : dto;
        final var idFuncionalidad = UUID.fromString(dtoToMap.getIdFuncionalidad());
        final var idTipoParametro = UUID.fromString(dtoToMap.getIdTipoParametro());
        final var activo = Boolean.parseBoolean(dtoToMap.getActivo());
        return CrearParametroDomain.create(UUIDHelper.getDefault(), dtoToMap.getNombre(), idFuncionalidad,
                idTipoParametro, activo);
    }
}
