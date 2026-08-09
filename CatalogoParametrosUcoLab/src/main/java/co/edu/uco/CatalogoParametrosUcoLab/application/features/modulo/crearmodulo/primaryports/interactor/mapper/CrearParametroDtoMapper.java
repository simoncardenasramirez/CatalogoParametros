package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearParametroDtoMapper {
    INSTANCE;

    public CrearParametroDomain toDomain(final CrearParametroDtoRequest dto) {
        var dtoToMap = dto == null ? new CrearParametroDtoRequest() : dto;
        validateStringFields(dtoToMap);
        final var idFuncionalidad = parseUUID(dtoToMap.getIdFuncionalidad(), "El identificador de la funcionalidad no es valido.");
        final var idTipoParametro = parseUUID(dtoToMap.getIdTipoParametro(), "El identificador del tipo de parametro no es valido.");
        final var activo = parseBoolean(dtoToMap.getActivo(), "El estado activo debe ser 'true' o 'false'.");
        return CrearParametroDomain.create(UUIDHelper.getDefault(), dtoToMap.getNombre(), idFuncionalidad,
                idTipoParametro, activo);
    }

    private void validateStringFields(final CrearParametroDtoRequest dto) {
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
