package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroFuncionalidadIsValidRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ParametroFuncionalidadIsValidRuleImpl implements ParametroFuncionalidadIsValidRule {

    @Override
    public void execute(final ParametroDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getIdFuncionalidad())) {
            throw new ParametroException("La funcionalidad asociada al parametro es obligatoria.");
        }
    }
}
