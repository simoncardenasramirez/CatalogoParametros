package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.crearfuncionalidadimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadModuloExistsRule;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.RangoFechasIsValidRule;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.CrearFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNameIsNotEmptyRule;

@Service
public class CrearFuncionalidadRuleValidatorImpl implements CrearFuncionalidadRuleValidator {

    private final FuncionalidadNameIsNotNullRule funcionalidadNameIsNotNullRule;
    private final FuncionalidadNameIsNotEmptyRule funcionalidadNameIsNotEmptyRule;
    private final FuncionalidadNameDoesNotExistRule funcionalidadNameDoesNotExistRule;
    private final FuncionalidadModuloExistsRule funcionalidadModuloExistsRule;

    public CrearFuncionalidadRuleValidatorImpl(final FuncionalidadNameIsNotNullRule funcionalidadNameIsNotNullRule,
                                               final FuncionalidadNameIsNotEmptyRule funcionalidadNameIsNotEmptyRule,
                                               final FuncionalidadNameDoesNotExistRule funcionalidadNameDoesNotExistRule, FuncionalidadModuloExistsRule funcionalidadModuloExistsRule) {
        this.funcionalidadNameIsNotNullRule = funcionalidadNameIsNotNullRule;
        this.funcionalidadNameIsNotEmptyRule = funcionalidadNameIsNotEmptyRule;
        this.funcionalidadNameDoesNotExistRule = funcionalidadNameDoesNotExistRule;
        this.funcionalidadModuloExistsRule = funcionalidadModuloExistsRule;
    }

    @Override
    public void validate(final CrearFuncionalidadDomain data) {
        funcionalidadNameIsNotNullRule.execute(data);
        funcionalidadNameIsNotEmptyRule.execute(data);
        funcionalidadNameDoesNotExistRule.execute(data);
        funcionalidadModuloExistsRule.execute(data);
        RangoFechasIsValidRule.execute(data.getFechaInicio(), data.getFechaFinal());
    }
}
