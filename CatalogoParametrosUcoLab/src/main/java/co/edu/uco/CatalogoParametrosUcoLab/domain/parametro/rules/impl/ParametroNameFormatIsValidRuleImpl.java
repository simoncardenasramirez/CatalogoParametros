package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameFormatIsValidRule;

@Service
public final class ParametroNameFormatIsValidRuleImpl implements ParametroNameFormatIsValidRule {

    private static final String VALID_NAME_PATTERN = "^[A-Za-z0-9_.-]+$";

    @Override
    public void execute(final ParametroDomain data) {
        if (!data.getNombre().matches(VALID_NAME_PATTERN)) {
            throw new ParametroException("El nombre del parametro solo puede contener letras, numeros, guion, punto y guion bajo.");
        }
    }
}
