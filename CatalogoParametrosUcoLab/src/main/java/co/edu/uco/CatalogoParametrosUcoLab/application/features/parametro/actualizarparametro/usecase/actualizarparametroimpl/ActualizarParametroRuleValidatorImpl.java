package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.actualizarparametroimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.ActualizarParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroFuncionalidadExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroFuncionalidadIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroTipoParametroIsValidRule;

@Service
public class ActualizarParametroRuleValidatorImpl implements ActualizarParametroRuleValidator {

    private final ActualizarParametroNameIsNotNullRule parametroNameIsNotNullRule;
    private final ActualizarParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule;
    private final ActualizarParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule;
    private final ActualizarParametroFuncionalidadExistsRule parametroFuncionalidadExistsRule;
    private final ActualizarParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule;
    private final ActualizarParametroNameDoesNotExistRule parametroNameDoesNotExistRule;

    public ActualizarParametroRuleValidatorImpl(final ActualizarParametroNameIsNotNullRule parametroNameIsNotNullRule,
            final ActualizarParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule,
            final ActualizarParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule,
            final ActualizarParametroFuncionalidadExistsRule parametroFuncionalidadExistsRule,
            final ActualizarParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule,
            final ActualizarParametroNameDoesNotExistRule parametroNameDoesNotExistRule) {
        this.parametroNameIsNotNullRule = parametroNameIsNotNullRule;
        this.parametroNameIsNotEmptyRule = parametroNameIsNotEmptyRule;
        this.parametroFuncionalidadIsValidRule = parametroFuncionalidadIsValidRule;
        this.parametroFuncionalidadExistsRule = parametroFuncionalidadExistsRule;
        this.parametroTipoParametroIsValidRule = parametroTipoParametroIsValidRule;
        this.parametroNameDoesNotExistRule = parametroNameDoesNotExistRule;
    }

    @Override
    public void validate(final ActualizarParametroDomain data) {
        parametroNameIsNotNullRule.execute(data);
        parametroNameIsNotEmptyRule.execute(data);
        parametroFuncionalidadIsValidRule.execute(data);
        parametroFuncionalidadExistsRule.execute(data);
        parametroTipoParametroIsValidRule.execute(data);
        parametroNameDoesNotExistRule.execute(data);
    }
}
