package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.actualizarmoduloimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.ActualizarModuloRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloAplicacionExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreIsNotNullRule;

@Service
public final class ActualizarModuloRuleValidatorImpl implements ActualizarModuloRuleValidator {

    private final ActualizarModuloNombreIsNotNullRule moduloNombreIsNotNullRule;
    private final ActualizarModuloNombreIsNotEmptyRule moduloNombreIsNotEmptyRule;
    private final ActualizarModuloNombreDoesNotExistRule moduloNombreDoesNotExistRule;
    private final ActualizarModuloAplicacionExistsRule moduloAplicacionExistsRule;
    private final ActualizarModuloIdExistsRule moduloIdExistsRule;

    public ActualizarModuloRuleValidatorImpl(
            final ActualizarModuloNombreIsNotNullRule moduloNombreIsNotNullRule,
            final ActualizarModuloNombreIsNotEmptyRule moduloNombreIsNotEmptyRule,
            final ActualizarModuloNombreDoesNotExistRule moduloNombreDoesNotExistRule,
            final ActualizarModuloAplicacionExistsRule moduloAplicacionExistsRule,
            final ActualizarModuloIdExistsRule moduloIdExistsRule) {
        this.moduloNombreIsNotNullRule = moduloNombreIsNotNullRule;
        this.moduloNombreIsNotEmptyRule = moduloNombreIsNotEmptyRule;
        this.moduloNombreDoesNotExistRule = moduloNombreDoesNotExistRule;
        this.moduloAplicacionExistsRule = moduloAplicacionExistsRule;
        this.moduloIdExistsRule = moduloIdExistsRule;
    }

    @Override
    public void validate(final ActualizarModuloDomain data) {
        moduloNombreIsNotNullRule.execute(data);
        moduloNombreIsNotEmptyRule.execute(data);
        moduloNombreDoesNotExistRule.execute(data);
        moduloAplicacionExistsRule.execute(data);
        moduloIdExistsRule.execute(data);
    }
}
