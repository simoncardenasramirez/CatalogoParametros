package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameIsNotEmptyRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

@Service
public final class ParametroNameIsNotEmptyRuleImpl implements ParametroNameIsNotEmptyRule {

    @Override
    public void execute(final CrearParametroDomain data) {
        if (TextHelper.isBlank(data.getNombre())) {
            throw ValidationException.build("El nombre del parametro no puede estar vacio.");
        }
    }
}
