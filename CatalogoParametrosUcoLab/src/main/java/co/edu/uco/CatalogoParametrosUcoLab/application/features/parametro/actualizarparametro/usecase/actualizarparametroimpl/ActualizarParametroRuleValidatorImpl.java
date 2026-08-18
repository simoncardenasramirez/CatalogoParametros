package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.actualizarparametroimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.ActualizarParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroFuncionalidadExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroFuncionalidadIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroTipoParametroIsValidRule;

@Service
public class ActualizarParametroRuleValidatorImpl implements ActualizarParametroRuleValidator {

    private final ActualizarParametroNombreIsNotNullRule parametroNombreIsNotNullRule;
    private final ActualizarParametroNombreIsNotEmptyRule parametroNombreIsNotEmptyRule;
    private final ActualizarParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule;
    private final ActualizarParametroFuncionalidadExistsRule parametroFuncionalidadExistsRule;
    private final ActualizarParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule;
    private final ActualizarParametroNombreDoesNotExistRule parametroNombreDoesNotExistRule;

    public ActualizarParametroRuleValidatorImpl(final ActualizarParametroNombreIsNotNullRule parametroNombreIsNotNullRule,
            final ActualizarParametroNombreIsNotEmptyRule parametroNombreIsNotEmptyRule,
            final ActualizarParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule,
            final ActualizarParametroFuncionalidadExistsRule parametroFuncionalidadExistsRule,
            final ActualizarParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule,
            final ActualizarParametroNombreDoesNotExistRule parametroNombreDoesNotExistRule) {
        this.parametroNombreIsNotNullRule = parametroNombreIsNotNullRule;
        this.parametroNombreIsNotEmptyRule = parametroNombreIsNotEmptyRule;
        this.parametroFuncionalidadIsValidRule = parametroFuncionalidadIsValidRule;
        this.parametroFuncionalidadExistsRule = parametroFuncionalidadExistsRule;
        this.parametroTipoParametroIsValidRule = parametroTipoParametroIsValidRule;
        this.parametroNombreDoesNotExistRule = parametroNombreDoesNotExistRule;
    }

    @Override
    public void validate(final ActualizarParametroDomain data) {
        parametroNombreIsNotNullRule.execute(data);
        parametroNombreIsNotEmptyRule.execute(data);
        parametroFuncionalidadIsValidRule.execute(data);
        parametroFuncionalidadExistsRule.execute(data);
        parametroTipoParametroIsValidRule.execute(data);
        parametroNombreDoesNotExistRule.execute(data);
    }
}
