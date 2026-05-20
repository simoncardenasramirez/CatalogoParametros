package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroValueIsNotNullRule;

@Service
public final class ParametroValueIsNotNullRuleImpl implements ParametroValueIsNotNullRule {

    @Override
    public void execute(final ParametroDomain data) {
        if (data.getValor() == null) {
            throw new ParametroException("El valor del parametro es obligatorio.");
        }
    }
}
