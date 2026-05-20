package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameIsNotNullRule;

@Service
public final class ParametroNameIsNotNullRuleImpl implements ParametroNameIsNotNullRule {

    @Override
    public void execute(final ParametroDomain data) {
        if (data == null || data.getNombre() == null) {
            throw new ParametroException("El nombre del parametro es obligatorio.");
        }
    }
}
