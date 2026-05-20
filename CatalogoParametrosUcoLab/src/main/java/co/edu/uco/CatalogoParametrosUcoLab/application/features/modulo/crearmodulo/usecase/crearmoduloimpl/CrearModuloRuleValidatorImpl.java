package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.crearmoduloimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.ModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreIsNotNullRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModuloRuleValidator;

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
