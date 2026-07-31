package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNombreIsNotEmptyRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

@Service
public final class ActualizarParametroNombreIsNotEmptyRuleImpl implements ActualizarParametroNombreIsNotEmptyRule {

    @Override
    public void execute(final ActualizarParametroDomain data) {
        if (TextHelper.isBlank(data.getNombre())) {
            throw new ParametroException("El nombre del parametro no puede estar vacio.");
        }
    }
}
