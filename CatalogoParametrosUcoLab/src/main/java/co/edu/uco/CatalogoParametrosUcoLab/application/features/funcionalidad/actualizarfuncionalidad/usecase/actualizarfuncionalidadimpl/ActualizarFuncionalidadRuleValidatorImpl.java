package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.actualizarfuncionalidadimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadModuloExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreIsNotNullRule;

@Service
public class ActualizarFuncionalidadRuleValidatorImpl implements ActualizarFuncionalidadRuleValidator {

    private final ActualizarFuncionalidadNombreIsNotNullRule funcionalidadNombreIsNotNullRule;
    private final ActualizarFuncionalidadNombreIsNotEmptyRule funcionalidadNombreIsNotEmptyRule;
    private final ActualizarFuncionalidadNombreDoesNotExistRule funcionalidadNombreDoesNotExistRule;
    private final ActualizarFuncionalidadModuloExistsRule funcionalidadModuloExistsRule;
    private final ActualizarFuncionalidadIdExistsRule funcionalidadIdExistsRule;

    public ActualizarFuncionalidadRuleValidatorImpl(
            final ActualizarFuncionalidadNombreIsNotNullRule funcionalidadNombreIsNotNullRule,
            final ActualizarFuncionalidadNombreIsNotEmptyRule funcionalidadNombreIsNotEmptyRule,
            final ActualizarFuncionalidadNombreDoesNotExistRule funcionalidadNombreDoesNotExistRule,
            final ActualizarFuncionalidadModuloExistsRule funcionalidadModuloExistsRule,
            final ActualizarFuncionalidadIdExistsRule funcionalidadIdExistsRule) {
        this.funcionalidadNombreIsNotNullRule = funcionalidadNombreIsNotNullRule;
        this.funcionalidadNombreIsNotEmptyRule = funcionalidadNombreIsNotEmptyRule;
        this.funcionalidadNombreDoesNotExistRule = funcionalidadNombreDoesNotExistRule;
        this.funcionalidadModuloExistsRule = funcionalidadModuloExistsRule;
        this.funcionalidadIdExistsRule = funcionalidadIdExistsRule;
    }

    @Override
    public void validate(final ActualizarFuncionalidadDomain data) {
        funcionalidadNombreIsNotNullRule.execute(data);
        funcionalidadNombreIsNotEmptyRule.execute(data);
        funcionalidadNombreDoesNotExistRule.execute(data);
        funcionalidadModuloExistsRule.execute(data);
        funcionalidadIdExistsRule.execute(data);
    }
}