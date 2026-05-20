package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroFuncionalidadIsValidRule;

@Service
public final class ParametroFuncionalidadIsValidRuleImpl implements ParametroFuncionalidadIsValidRule {

    @Override
    public void execute(final ParametroDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getIdFuncionalidad())) {
            throw new ParametroException("La funcionalidad asociada al parametro es obligatoria.");
        }
    }
}
