package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroTipoParametroIsValidRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ParametroTipoParametroIsValidRuleImpl implements ParametroTipoParametroIsValidRule {

    @Override
    public void execute(final CrearParametroDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getIdTipoParametro())) {
            throw new ParametroException("El tipo de parametro asociado es obligatorio.");
        }
    }
}
