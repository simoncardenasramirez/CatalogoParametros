package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.actualizarfuncionalidadimpl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.RangoFechasIsValidRule;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadModuloExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNameIsNotNullRule;

@Service
public class ActualizarFuncionalidadRuleValidatorImpl implements ActualizarFuncionalidadRuleValidator {

    private final ActualizarFuncionalidadNameIsNotNullRule funcionalidadNameIsNotNullRule;
    private final ActualizarFuncionalidadNameIsNotEmptyRule funcionalidadNameIsNotEmptyRule;
    private final ActualizarFuncionalidadNameDoesNotExistRule funcionalidadNameDoesNotExistRule;
    private final ActualizarFuncionalidadModuloExistsRule funcionalidadModuloExistsRule;
    private final ActualizarFuncionalidadIdExistsRule funcionalidadIdExistsRule;

    public ActualizarFuncionalidadRuleValidatorImpl(
            final ActualizarFuncionalidadNameIsNotNullRule funcionalidadNameIsNotNullRule,
            final ActualizarFuncionalidadNameIsNotEmptyRule funcionalidadNameIsNotEmptyRule,
            final ActualizarFuncionalidadNameDoesNotExistRule funcionalidadNameDoesNotExistRule,
            final ActualizarFuncionalidadModuloExistsRule funcionalidadModuloExistsRule,
            final ActualizarFuncionalidadIdExistsRule funcionalidadIdExistsRule) {
        this.funcionalidadNameIsNotNullRule = funcionalidadNameIsNotNullRule;
        this.funcionalidadNameIsNotEmptyRule = funcionalidadNameIsNotEmptyRule;
        this.funcionalidadNameDoesNotExistRule = funcionalidadNameDoesNotExistRule;
        this.funcionalidadModuloExistsRule = funcionalidadModuloExistsRule;
        this.funcionalidadIdExistsRule = funcionalidadIdExistsRule;
    }

    @Override
    public void validate(final ActualizarFuncionalidadDomain data) {
        funcionalidadNameIsNotNullRule.execute(data);
        funcionalidadNameIsNotEmptyRule.execute(data);
        funcionalidadNameDoesNotExistRule.execute(data);
        funcionalidadModuloExistsRule.execute(data);
        funcionalidadIdExistsRule.execute(data);
        RangoFechasIsValidRule.execute(data.getFechaInicio(), data.getFechaFinal());
    }
}
