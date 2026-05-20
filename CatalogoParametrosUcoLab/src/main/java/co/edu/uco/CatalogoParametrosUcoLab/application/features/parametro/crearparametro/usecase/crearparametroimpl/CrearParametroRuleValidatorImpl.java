package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.crearparametroimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.*;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametroRuleValidator;

@Service
public class CrearParametroRuleValidatorImpl implements CrearParametroRuleValidator {

    private final ParametroNameIsNotNullRule parametroNameIsNotNullRule;
    private final ParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule;
    private final ParametroNameLengthIsValidRule parametroNameLengthIsValidRule;
    private final ParametroNameFormatIsValidRule parametroNameFormatIsValidRule;
    private final ParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule;
    private final ParametroFuncionalidadExistsRule parametroFuncionalidadExistsRule;
    private final ParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule;
    private final ParametroNameDoesNotExistRule parametroNameDoesNotExistRule;

    public CrearParametroRuleValidatorImpl(final ParametroNameIsNotNullRule parametroNameIsNotNullRule,
            final ParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule,
            final ParametroNameLengthIsValidRule parametroNameLengthIsValidRule,
            final ParametroNameFormatIsValidRule parametroNameFormatIsValidRule,
            final ParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule,
            final ParametroFuncionalidadExistsRule parametroFuncionalidadExistsRule,
            final ParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule,
            final ParametroNameDoesNotExistRule parametroNameDoesNotExistRule) {
        this.parametroNameIsNotNullRule = parametroNameIsNotNullRule;
        this.parametroNameIsNotEmptyRule = parametroNameIsNotEmptyRule;
        this.parametroNameLengthIsValidRule = parametroNameLengthIsValidRule;
        this.parametroNameFormatIsValidRule = parametroNameFormatIsValidRule;
        this.parametroFuncionalidadIsValidRule = parametroFuncionalidadIsValidRule;
        this.parametroFuncionalidadExistsRule = parametroFuncionalidadExistsRule;
        this.parametroTipoParametroIsValidRule = parametroTipoParametroIsValidRule;
        this.parametroNameDoesNotExistRule = parametroNameDoesNotExistRule;
    }

    @Override
    public void validate(final ParametroDomain data) {
        parametroNameIsNotNullRule.execute(data);
        parametroNameIsNotEmptyRule.execute(data);
        parametroNameLengthIsValidRule.execute(data);
        parametroNameFormatIsValidRule.execute(data);
        parametroFuncionalidadIsValidRule.execute(data);
        parametroFuncionalidadExistsRule.execute(data);
        parametroTipoParametroIsValidRule.execute(data);
        parametroNameDoesNotExistRule.execute(data);
    }
}
