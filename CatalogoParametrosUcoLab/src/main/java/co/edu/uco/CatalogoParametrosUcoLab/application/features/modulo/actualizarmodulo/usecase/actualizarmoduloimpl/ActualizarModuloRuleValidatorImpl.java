package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.actualizarmoduloimpl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.RangoFechasIsValidRule;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.ActualizarModuloRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloAplicacionExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNameIsNotNullRule;

@Service
public final class ActualizarModuloRuleValidatorImpl implements ActualizarModuloRuleValidator {

    private final ActualizarModuloNameIsNotNullRule moduloNameIsNotNullRule;
    private final ActualizarModuloNameIsNotEmptyRule moduloNameIsNotEmptyRule;
    private final ActualizarModuloNameDoesNotExistRule moduloNameDoesNotExistRule;
    private final ActualizarModuloAplicacionExistsRule moduloAplicacionExistsRule;
    private final ActualizarModuloIdExistsRule moduloIdExistsRule;

    public ActualizarModuloRuleValidatorImpl(
            final ActualizarModuloNameIsNotNullRule moduloNameIsNotNullRule,
            final ActualizarModuloNameIsNotEmptyRule moduloNameIsNotEmptyRule,
            final ActualizarModuloNameDoesNotExistRule moduloNameDoesNotExistRule,
            final ActualizarModuloAplicacionExistsRule moduloAplicacionExistsRule,
            final ActualizarModuloIdExistsRule moduloIdExistsRule) {
        this.moduloNameIsNotNullRule = moduloNameIsNotNullRule;
        this.moduloNameIsNotEmptyRule = moduloNameIsNotEmptyRule;
        this.moduloNameDoesNotExistRule = moduloNameDoesNotExistRule;
        this.moduloAplicacionExistsRule = moduloAplicacionExistsRule;
        this.moduloIdExistsRule = moduloIdExistsRule;
    }

    @Override
    public void validate(final ActualizarModuloDomain data) {
        moduloNameIsNotNullRule.execute(data);
        moduloNameIsNotEmptyRule.execute(data);
        moduloNameDoesNotExistRule.execute(data);
        moduloAplicacionExistsRule.execute(data);
        moduloIdExistsRule.execute(data);
        RangoFechasIsValidRule.execute(data.getFechaInicio(), data.getFechaFinal());
    }
}
