package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.actualizaraplicacionimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.ActualizarAplicacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionOrganizacionExistsRule;

@Service
public class ActualizarAplicacionRuleValidatorImpl implements ActualizarAplicacionRuleValidator {

    private final ActualizarAplicacionNombreIsNotNullRule aplicacionNombreIsNotNullRule;
    private final ActualizarAplicacionNombreIsNotEmptyRule aplicacionNombreIsNotEmptyRule;
    private final ActualizarAplicacionNombreDoesNotExistRule aplicacionNombreDoesNotExistRule;
    private final ActualizarAplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule;
    private final ActualizarAplicacionIdExistsRule aplicacionIdExistsRule;

    public ActualizarAplicacionRuleValidatorImpl(
            final ActualizarAplicacionNombreIsNotNullRule aplicacionNombreIsNotNullRule,
            final ActualizarAplicacionNombreIsNotEmptyRule aplicacionNombreIsNotEmptyRule,
            final ActualizarAplicacionNombreDoesNotExistRule aplicacionNombreDoesNotExistRule,
            final ActualizarAplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule,
            final ActualizarAplicacionIdExistsRule aplicacionIdExistsRule) {
        this.aplicacionNombreIsNotNullRule = aplicacionNombreIsNotNullRule;
        this.aplicacionNombreIsNotEmptyRule = aplicacionNombreIsNotEmptyRule;
        this.aplicacionNombreDoesNotExistRule = aplicacionNombreDoesNotExistRule;
        this.aplicacionOrganizacionExistsRule = aplicacionOrganizacionExistsRule;
        this.aplicacionIdExistsRule = aplicacionIdExistsRule;
    }

    @Override
    public void validate(final ActualizarAplicacionDomain data) {
        aplicacionNombreIsNotNullRule.execute(data);
        aplicacionNombreIsNotEmptyRule.execute(data);
        aplicacionNombreDoesNotExistRule.execute(data);
        aplicacionOrganizacionExistsRule.execute(data);
        aplicacionIdExistsRule.execute(data);
    }
}
