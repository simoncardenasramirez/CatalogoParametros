package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.crearaplicacionimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionOrganizacionExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacionRuleValidator;

@Service
public class CrearAplicacionRuleValidatorImpl implements CrearAplicacionRuleValidator {

    private final AplicacionNombreIsNotNullRule aplicacionNombreIsNotNullRule;
    private final AplicacionNombreIsNotEmptyRule aplicacionNombreIsNotEmptyRule;
    private final AplicacionNombreDoesNotExistRule aplicacionNombreDoesNotExistRule;
    private final AplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule;

    public CrearAplicacionRuleValidatorImpl(final AplicacionNombreIsNotNullRule aplicacionNombreIsNotNullRule,
                                               final AplicacionNombreIsNotEmptyRule aplicacionNombreIsNotEmptyRule,
                                               final AplicacionNombreDoesNotExistRule aplicacionNombreDoesNotExistRule,
                                               final AplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule) {
        this.aplicacionNombreIsNotNullRule = aplicacionNombreIsNotNullRule;
        this.aplicacionNombreIsNotEmptyRule = aplicacionNombreIsNotEmptyRule;
        this.aplicacionNombreDoesNotExistRule = aplicacionNombreDoesNotExistRule;
        this.aplicacionOrganizacionExistsRule = aplicacionOrganizacionExistsRule;
    }

    @Override
    public void validate(final CrearAplicacionDomain data) {
        aplicacionNombreIsNotNullRule.execute(data);
        aplicacionNombreIsNotEmptyRule.execute(data);
        aplicacionNombreDoesNotExistRule.execute(data);
        aplicacionOrganizacionExistsRule.execute(data);
    }
}
