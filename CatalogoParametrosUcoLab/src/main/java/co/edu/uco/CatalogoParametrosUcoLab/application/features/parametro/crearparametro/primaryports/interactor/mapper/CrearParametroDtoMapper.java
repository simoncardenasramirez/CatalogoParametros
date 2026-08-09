package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearParametroDtoMapper {
    INSTANCE;

    public CrearParametroDomain toDomain(final CrearParametroDtoRequest dto) {
        final var dtoInput = toDtoInput(dto);
        return toDomain(dtoInput);
    }

    public CrearParametroDtoInput toDtoInput(final CrearParametroDtoRequest dto) {
        var dtoToMap = dto == null ? new CrearParametroDtoRequest() : dto;
        final var idFuncionalidad = parseUUID(dtoToMap.getIdFuncionalidad(), "El identificador de la funcionalidad no es valido.");
        final var idTipoParametro = parseUUID(dtoToMap.getIdTipoParametro(), "El identificador del tipo de parametro no es valido.");
        final var activo = parseBoolean(dtoToMap.getActivo(), "El estado activo debe ser 'true' o 'false'.");
        return CrearParametroDtoInput.create(dtoToMap.getNombre(), idFuncionalidad, idTipoParametro, activo);
    }

    public CrearParametroDomain toDomain(final CrearParametroDtoInput dtoInput) {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), dtoInput.getNombre(), dtoInput.getIdFuncionalidad(),
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
