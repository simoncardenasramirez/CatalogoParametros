package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameIsNotEmptyRule;

@Service
public final class ParametroNameIsNotEmptyRuleImpl implements ParametroNameIsNotEmptyRule {

    @Override
    public void execute(final ParametroDomain data) {
        if (TextHelper.isBlank(data.getNombre())) {
            throw new ParametroException("El nombre del parametro no puede estar vacio.");
        }
    }
}
