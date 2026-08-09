package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public enum ActualizarParametroDtoMapper {
    INSTANCE;

    public ActualizarParametroDomain toDomain(final UUID id, final ActualizarParametroDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(id, dtoInput);
    }

    public ActualizarParametroDtoInput toDtoInput(final ActualizarParametroDtoRequest dto) {
        var dtoToMap = dto == null ? new ActualizarParametroDtoRequest() : dto;
        final var idFuncionalidad = parseUUID(dtoToMap.getIdFuncionalidad(), "El identificador de la funcionalidad no es valido.");
        final var idTipoParametro = parseUUID(dtoToMap.getIdTipoParametro(), "El identificador del tipo de parametro no es valido.");
        final var activo = parseBoolean(dtoToMap.getActivo(), "El estado activo debe ser 'true' o 'false'.");
        return ActualizarParametroDtoInput.create(dtoToMap.getNombre(), idFuncionalidad, idTipoParametro, activo);
    }

    public ActualizarParametroDomain toDomain(final UUID id, final ActualizarParametroDtoInput dtoInput) {
        return ActualizarParametroDomain.create(id, dtoInput.getNombre(), dtoInput.getIdFuncionalidad(),
                dtoInput.getIdTipoParametro(), dtoInput.isActivo());
    }

    private UUID parseUUID(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            throw new ParametroException(errorMessage);
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new ParametroException(errorMessage + " Valor recibido: " + value);
        }
    }

    private boolean parseBoolean(final String value, final String errorMessage) {
        if (TextHelper.isBlank(value)) {
            throw new ParametroException(errorMessage);
        }
        if (!"true".equals(value) && !"false".equals(value)) {
            throw new ParametroException(errorMessage + " Valor recibido: " + value);
        }
        return Boolean.parseBoolean(value);
    }
}
