package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameFormatIsValidRule;
import org.springframework.stereotype.Service;


@Service
public final class ParametroNameFormatIsValidRuleImpl implements ParametroNameFormatIsValidRule {

    private static final String VALID_NAME_PATTERN = "^[A-Za-z0-9_.-]+$";

    @Override
    public void execute(final CrearParametroDomain data) {
        if (!data.getNombre().matches(VALID_NAME_PATTERN)) {
            throw new ParametroException("El nombre del parametro solo puede contener letras, numeros, guion, punto y guion bajo.");
        }
    }
}
