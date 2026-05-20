package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.funcionalidad.impl;

import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.FuncionalidadModuloExistsRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.funcionalidad.CrearFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.FuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.FuncionalidadNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.FuncionalidadNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.FuncionalidadNombreIsNotEmptyRule;

@Service
public class CrearFuncionalidadRuleValidatorImpl implements CrearFuncionalidadRuleValidator {

    private final FuncionalidadNombreIsNotNullRule funcionalidadNombreIsNotNullRule;
    private final FuncionalidadNombreIsNotEmptyRule funcionalidadNombreIsNotEmptyRule;
    private final FuncionalidadNombreDoesNotExistRule funcionalidadNombreDoesNotExistRule;
    private final FuncionalidadModuloExistsRule funcionalidadModuloExistsRule;

    public CrearFuncionalidadRuleValidatorImpl(final FuncionalidadNombreIsNotNullRule funcionalidadNombreIsNotNullRule,
                                               final FuncionalidadNombreIsNotEmptyRule funcionalidadNombreIsNotEmptyRule,
                                               final FuncionalidadNombreDoesNotExistRule funcionalidadNombreDoesNotExistRule, FuncionalidadModuloExistsRule funcionalidadModuloExistsRule) {
        this.funcionalidadNombreIsNotNullRule = funcionalidadNombreIsNotNullRule;
        this.funcionalidadNombreIsNotEmptyRule = funcionalidadNombreIsNotEmptyRule;
        this.funcionalidadNombreDoesNotExistRule = funcionalidadNombreDoesNotExistRule;
        this.funcionalidadModuloExistsRule = funcionalidadModuloExistsRule;
    }

    @Override
    public void validate(final FuncionalidadDomain data) {
        funcionalidadNombreIsNotNullRule.execute(data);
        funcionalidadNombreIsNotEmptyRule.execute(data);
        funcionalidadNombreDoesNotExistRule.execute(data);
        funcionalidadModuloExistsRule.execute(data);
    }
}