package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameIsNotNullRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class ParametroNameIsNotNullRuleImpl implements ParametroNameIsNotNullRule {

    @Override
    public void execute(final CrearParametroDomain data) {
        if (data == null || data.getNombre() == null) {
            throw ValidationException.build("El nombre del parametro es obligatorio.");
        }
    }
}
