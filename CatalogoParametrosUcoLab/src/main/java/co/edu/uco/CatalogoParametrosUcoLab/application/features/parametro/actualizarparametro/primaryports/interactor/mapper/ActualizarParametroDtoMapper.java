package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public enum ActualizarParametroDtoMapper {
    INSTANCE;

    public ActualizarParametroDomain toDomain(final UUID id, final ActualizarParametroDto dto) {
        var dtoToMap = dto == null ? new ActualizarParametroDto() : dto;
        validateStringFields(dtoToMap);
        final var idFuncionalidad = parseUUID(dtoToMap.getIdFuncionalidad(), "El identificador de la funcionalidad no es valido.");
        final var idTipoParametro = parseUUID(dtoToMap.getIdTipoParametro(), "El identificador del tipo de parametro no es valido.");
        final var activo = parseBoolean(dtoToMap.getActivo(), "El estado activo debe ser 'true' o 'false'.");
        return ActualizarParametroDomain.create(id, dtoToMap.getNombre(), idFuncionalidad,
                idTipoParametro, activo);
    }

    private void validateStringFields(final ActualizarParametroDto dto) {
        if (TextHelper.isBlank(dto.getNombre())) {
            throw new ParametroException("El nombre del parametro es obligatorio.");
        }
        if (dto.getNombre().length() < 3 || dto.getNombre().length() > 50) {
            throw new ParametroException("El nombre debe tener entre 3 y 50 caracteres.");
        }
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
