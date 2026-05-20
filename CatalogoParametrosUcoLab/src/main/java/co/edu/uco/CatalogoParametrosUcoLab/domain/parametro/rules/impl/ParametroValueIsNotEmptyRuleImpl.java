package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroValueIsNotEmptyRule;

@Service
public final class ParametroValueIsNotEmptyRuleImpl implements ParametroValueIsNotEmptyRule {

    @Override
    public void execute(final ParametroDomain data) {
        if (TextHelper.isBlank(data.getValor())) {
            throw new ParametroException("El valor del parametro no puede estar vacio.");
        }
    }
}
