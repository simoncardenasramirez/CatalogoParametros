package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroValueLengthIsValidRule;

@Service
public final class ParametroValueLengthIsValidRuleImpl implements ParametroValueLengthIsValidRule {

    private static final int MAX_LENGTH = 250;

    @Override
    public void execute(final ParametroDomain data) {
        if (data.getValor().length() > MAX_LENGTH) {
            throw new ParametroException("El valor del parametro no puede superar 250 caracteres.");
        }
    }
}
