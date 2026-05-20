package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.parametro.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.parametro.CrearParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroFuncionalidadIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameFormatIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameLengthIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroTipoParametroIsValidRule;

@Service
public class CrearParametroRuleValidatorImpl implements CrearParametroRuleValidator {

    private final ParametroNameIsNotNullRule parametroNameIsNotNullRule;
    private final ParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule;
    private final ParametroNameLengthIsValidRule parametroNameLengthIsValidRule;
    private final ParametroNameFormatIsValidRule parametroNameFormatIsValidRule;
    private final ParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule;
    private final ParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule;
    private final ParametroNameDoesNotExistRule parametroNameDoesNotExistRule;

    public CrearParametroRuleValidatorImpl(final ParametroNameIsNotNullRule parametroNameIsNotNullRule,
            final ParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule,
            final ParametroNameLengthIsValidRule parametroNameLengthIsValidRule,
            final ParametroNameFormatIsValidRule parametroNameFormatIsValidRule,
            final ParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule,
            final ParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule,
            final ParametroNameDoesNotExistRule parametroNameDoesNotExistRule) {
        this.parametroNameIsNotNullRule = parametroNameIsNotNullRule;
        this.parametroNameIsNotEmptyRule = parametroNameIsNotEmptyRule;
        this.parametroNameLengthIsValidRule = parametroNameLengthIsValidRule;
        this.parametroNameFormatIsValidRule = parametroNameFormatIsValidRule;
        this.parametroFuncionalidadIsValidRule = parametroFuncionalidadIsValidRule;
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
        parametroTipoParametroIsValidRule.execute(data);
        parametroNameDoesNotExistRule.execute(data);
    }
}
