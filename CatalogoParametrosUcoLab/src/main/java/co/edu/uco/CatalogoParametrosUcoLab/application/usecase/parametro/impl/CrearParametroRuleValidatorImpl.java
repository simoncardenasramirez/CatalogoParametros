package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.parametro.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.parametro.CrearParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroDescripcionLengthIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameFormatIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameLengthIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroValueIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroValueIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroValueLengthIsValidRule;

@Service
public class CrearParametroRuleValidatorImpl implements CrearParametroRuleValidator {

    private final ParametroNameIsNotNullRule parametroNameIsNotNullRule;
    private final ParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule;
    private final ParametroNameLengthIsValidRule parametroNameLengthIsValidRule;
    private final ParametroNameFormatIsValidRule parametroNameFormatIsValidRule;
    private final ParametroValueIsNotNullRule parametroValueIsNotNullRule;
    private final ParametroValueIsNotEmptyRule parametroValueIsNotEmptyRule;
    private final ParametroValueLengthIsValidRule parametroValueLengthIsValidRule;
    private final ParametroDescripcionLengthIsValidRule parametroDescripcionLengthIsValidRule;
    private final ParametroNameDoesNotExistRule parametroNameDoesNotExistRule;

    public CrearParametroRuleValidatorImpl(final ParametroNameIsNotNullRule parametroNameIsNotNullRule,
            final ParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule,
            final ParametroNameLengthIsValidRule parametroNameLengthIsValidRule,
            final ParametroNameFormatIsValidRule parametroNameFormatIsValidRule,
            final ParametroValueIsNotNullRule parametroValueIsNotNullRule,
            final ParametroValueIsNotEmptyRule parametroValueIsNotEmptyRule,
            final ParametroValueLengthIsValidRule parametroValueLengthIsValidRule,
            final ParametroDescripcionLengthIsValidRule parametroDescripcionLengthIsValidRule,
            final ParametroNameDoesNotExistRule parametroNameDoesNotExistRule) {
        this.parametroNameIsNotNullRule = parametroNameIsNotNullRule;
        this.parametroNameIsNotEmptyRule = parametroNameIsNotEmptyRule;
        this.parametroNameLengthIsValidRule = parametroNameLengthIsValidRule;
        this.parametroNameFormatIsValidRule = parametroNameFormatIsValidRule;
        this.parametroValueIsNotNullRule = parametroValueIsNotNullRule;
        this.parametroValueIsNotEmptyRule = parametroValueIsNotEmptyRule;
        this.parametroValueLengthIsValidRule = parametroValueLengthIsValidRule;
        this.parametroDescripcionLengthIsValidRule = parametroDescripcionLengthIsValidRule;
        this.parametroNameDoesNotExistRule = parametroNameDoesNotExistRule;
    }

    @Override
    public void validate(final ParametroDomain data) {
        parametroNameIsNotNullRule.execute(data);
        parametroNameIsNotEmptyRule.execute(data);
        parametroNameLengthIsValidRule.execute(data);
        parametroNameFormatIsValidRule.execute(data);
        parametroValueIsNotNullRule.execute(data);
        parametroValueIsNotEmptyRule.execute(data);
        parametroValueLengthIsValidRule.execute(data);
        parametroDescripcionLengthIsValidRule.execute(data);
        parametroNameDoesNotExistRule.execute(data);
    }
}
