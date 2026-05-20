package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroDescripcionLengthIsValidRule;

@Service
public final class ParametroDescripcionLengthIsValidRuleImpl implements ParametroDescripcionLengthIsValidRule {

    private static final int MAX_LENGTH = 500;

    @Override
    public void execute(final ParametroDomain data) {
        if (data.getDescripcion().length() > MAX_LENGTH) {
            throw new ParametroException("La descripcion del parametro no puede superar 500 caracteres.");
        }
    }
}
