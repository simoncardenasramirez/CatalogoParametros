package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.modulo.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.modulo.CrearModuloRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.ModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.rules.ModuloNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.rules.ModuloNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.rules.ModuloNombreIsNotNullRule;

@Service
public class CrearModuloRuleValidatorImpl implements CrearModuloRuleValidator {

    private final ModuloNombreIsNotNullRule moduloNombreIsNotNullRule;
    private final ModuloNombreIsNotEmptyRule moduloNombreIsNotEmptyRule;
    private final ModuloNombreDoesNotExistRule moduloNombreDoesNotExistRule;

    public CrearModuloRuleValidatorImpl(final ModuloNombreIsNotNullRule moduloNombreIsNotNullRule,
            final ModuloNombreIsNotEmptyRule moduloNombreIsNotEmptyRule,
            final ModuloNombreDoesNotExistRule moduloNombreDoesNotExistRule) {
        this.moduloNombreIsNotNullRule = moduloNombreIsNotNullRule;
        this.moduloNombreIsNotEmptyRule = moduloNombreIsNotEmptyRule;
        this.moduloNombreDoesNotExistRule = moduloNombreDoesNotExistRule;
    }

    @Override
    public void validate(final ModuloDomain data) {
        moduloNombreIsNotNullRule.execute(data);
        moduloNombreIsNotEmptyRule.execute(data);
        moduloNombreDoesNotExistRule.execute(data);
    }
}
