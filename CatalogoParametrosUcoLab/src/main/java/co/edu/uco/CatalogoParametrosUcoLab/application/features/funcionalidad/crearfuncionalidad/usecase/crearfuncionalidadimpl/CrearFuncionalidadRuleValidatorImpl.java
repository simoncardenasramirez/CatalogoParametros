package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.crearfuncionalidadimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadModuloExistsRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.CrearFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNombreIsNotEmptyRule;

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
    public void validate(final CrearFuncionalidadDomain data) {
        funcionalidadNombreIsNotNullRule.execute(data);
        funcionalidadNombreIsNotEmptyRule.execute(data);
        funcionalidadNombreDoesNotExistRule.execute(data);
        funcionalidadModuloExistsRule.execute(data);
    }
}