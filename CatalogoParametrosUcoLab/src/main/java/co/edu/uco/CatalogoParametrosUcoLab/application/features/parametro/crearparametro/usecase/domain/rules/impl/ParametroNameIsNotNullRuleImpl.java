package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameIsNotNullRule;
import org.springframework.stereotype.Service;


@Service
public final class ParametroNameIsNotNullRuleImpl implements ParametroNameIsNotNullRule {

    @Override
    public void execute(final ParametroDomain data) {
        if (data == null || data.getNombre() == null) {
            throw new ParametroException("El nombre del parametro es obligatorio.");
        }
    }
}
