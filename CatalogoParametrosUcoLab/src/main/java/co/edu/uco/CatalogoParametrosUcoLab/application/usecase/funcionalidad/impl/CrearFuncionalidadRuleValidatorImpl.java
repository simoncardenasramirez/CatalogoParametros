package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.funcionalidad.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.funcionalidad.CrearFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.FuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.FuncionalidadNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.FuncionalidadNombreIsNotEmptyRule;

@Service
public class CrearFuncionalidadRuleValidatorImpl implements CrearFuncionalidadRuleValidator {

    private final FuncionalidadNombreIsNotNullRule funcionalidadNombreIsNotNullRule;
    private final FuncionalidadNombreIsNotEmptyRule funcionalidadNombreIsNotEmptyRule;

    public CrearFuncionalidadRuleValidatorImpl(final FuncionalidadNombreIsNotNullRule funcionalidadNombreIsNotNullRule,
            final FuncionalidadNombreIsNotEmptyRule funcionalidadNombreIsNotEmptyRule) {
        this.funcionalidadNombreIsNotNullRule = funcionalidadNombreIsNotNullRule;
        this.funcionalidadNombreIsNotEmptyRule = funcionalidadNombreIsNotEmptyRule;
    }

    @Override
    public void validate(final FuncionalidadDomain data) {
        funcionalidadNombreIsNotNullRule.execute(data);
        funcionalidadNombreIsNotEmptyRule.execute(data);
    }
}