package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.crearmoduloimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloAplicacionExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNameIsNotNullRule;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.RangoFechasIsValidRule;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModuloRuleValidator;

@Service
public class CrearModuloRuleValidatorImpl implements CrearModuloRuleValidator {

    private final ModuloNameIsNotNullRule moduloNameIsNotNullRule;
    private final ModuloNameIsNotEmptyRule moduloNameIsNotEmptyRule;
    private final ModuloNameDoesNotExistRule moduloNameDoesNotExistRule;
    private final ModuloAplicacionExistsRule moduloAplicacionExistsRule;

    public CrearModuloRuleValidatorImpl(final ModuloNameIsNotNullRule moduloNameIsNotNullRule,
            final ModuloNameIsNotEmptyRule moduloNameIsNotEmptyRule,
            final ModuloNameDoesNotExistRule moduloNameDoesNotExistRule,
            final ModuloAplicacionExistsRule moduloAplicacionExistsRule) {
        this.moduloNameIsNotNullRule = moduloNameIsNotNullRule;
        this.moduloNameIsNotEmptyRule = moduloNameIsNotEmptyRule;
        this.moduloNameDoesNotExistRule = moduloNameDoesNotExistRule;
        this.moduloAplicacionExistsRule = moduloAplicacionExistsRule;
    }

    @Override
    public void validate(final CrearModuloDomain data) {
        moduloNameIsNotNullRule.execute(data);
        moduloNameIsNotEmptyRule.execute(data);
        moduloNameDoesNotExistRule.execute(data);
        moduloAplicacionExistsRule.execute(data);
        RangoFechasIsValidRule.execute(data.getFechaInicio(), data.getFechaFinal());
    }
}
