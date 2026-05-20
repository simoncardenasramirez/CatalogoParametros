package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameLengthIsValidRule;
import org.springframework.stereotype.Service;


@Service
public final class ParametroNameLengthIsValidRuleImpl implements ParametroNameLengthIsValidRule {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 120;

    @Override
    public void execute(final ParametroDomain data) {
        var length = data.getNombre().length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new ParametroException("El nombre del parametro debe tener entre 3 y 120 caracteres.");
        }
    }
}
