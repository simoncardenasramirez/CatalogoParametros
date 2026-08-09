package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;

public enum ActualizarParametroDtoMapper {
    INSTANCE;

    public ActualizarParametroDomain toDomain(final UUID id, final ActualizarParametroDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(id, dtoInput);
    }

    public ActualizarParametroDtoInput toDtoInput(final ActualizarParametroDtoRequest dto) {
        var dtoToMap = dto == null ? new ActualizarParametroDtoRequest() : dto;
        final var idFuncionalidad = UUID.fromString(dtoToMap.getIdFuncionalidad());
        final var idTipoParametro = UUID.fromString(dtoToMap.getIdTipoParametro());
        final var activo = Boolean.parseBoolean(dtoToMap.getActivo());
        return ActualizarParametroDtoInput.create(dtoToMap.getNombre(), idFuncionalidad, idTipoParametro, activo);
    }

    public ActualizarParametroDomain toDomain(final UUID id, final ActualizarParametroDtoInput dtoInput) {
        return ActualizarParametroDomain.create(id, dtoInput.getNombre(), dtoInput.getIdFuncionalidad(),
                dtoInput.getIdTipoParametro(), dtoInput.isActivo());
    }
}
