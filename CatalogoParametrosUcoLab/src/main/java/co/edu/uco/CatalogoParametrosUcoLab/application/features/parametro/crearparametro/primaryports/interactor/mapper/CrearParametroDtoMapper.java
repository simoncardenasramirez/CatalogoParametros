package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearParametroDtoMapper {
    INSTANCE;

    public CrearParametroDomain toDomain(final CrearParametroDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(dtoInput);
    }

    public CrearParametroDtoInput toDtoInput(final CrearParametroDtoRequest dto) {
        var dtoToMap = dto == null ? new CrearParametroDtoRequest() : dto;
        final var idFuncionalidad = UUID.fromString(dtoToMap.getIdFuncionalidad());
        final var idTipoParametro = UUID.fromString(dtoToMap.getIdTipoParametro());
        final var activo = Boolean.parseBoolean(dtoToMap.getActivo());
        return CrearParametroDtoInput.create(dtoToMap.getNombre(), idFuncionalidad, idTipoParametro, activo);
    }

    public CrearParametroDomain toDomain(final CrearParametroDtoInput dtoInput) {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), dtoInput.getNombre(), dtoInput.getIdFuncionalidad(),
                dtoInput.getIdTipoParametro(), dtoInput.isActivo());
    }
}
