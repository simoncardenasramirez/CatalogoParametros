package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.actualizaraplicacionimpl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.RangoFechasIsValidRule;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.ActualizarAplicacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionOrganizacionExistsRule;

@Service
public class ActualizarAplicacionRuleValidatorImpl implements ActualizarAplicacionRuleValidator {

    private final ActualizarAplicacionNameIsNotNullRule aplicacionNameIsNotNullRule;
    private final ActualizarAplicacionNameIsNotEmptyRule aplicacionNameIsNotEmptyRule;
    private final ActualizarAplicacionNameDoesNotExistRule aplicacionNameDoesNotExistRule;
    private final ActualizarAplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule;
    private final ActualizarAplicacionIdExistsRule aplicacionIdExistsRule;

    public ActualizarAplicacionRuleValidatorImpl(
            final ActualizarAplicacionNameIsNotNullRule aplicacionNameIsNotNullRule,
            final ActualizarAplicacionNameIsNotEmptyRule aplicacionNameIsNotEmptyRule,
            final ActualizarAplicacionNameDoesNotExistRule aplicacionNameDoesNotExistRule,
            final ActualizarAplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule,
            final ActualizarAplicacionIdExistsRule aplicacionIdExistsRule) {
        this.aplicacionNameIsNotNullRule = aplicacionNameIsNotNullRule;
        this.aplicacionNameIsNotEmptyRule = aplicacionNameIsNotEmptyRule;
        this.aplicacionNameDoesNotExistRule = aplicacionNameDoesNotExistRule;
        this.aplicacionOrganizacionExistsRule = aplicacionOrganizacionExistsRule;
        this.aplicacionIdExistsRule = aplicacionIdExistsRule;
    }

    @Override
    public void validate(final ActualizarAplicacionDomain data) {
        aplicacionNameIsNotNullRule.execute(data);
        aplicacionNameIsNotEmptyRule.execute(data);
        aplicacionNameDoesNotExistRule.execute(data);
        aplicacionOrganizacionExistsRule.execute(data);
        aplicacionIdExistsRule.execute(data);
        RangoFechasIsValidRule.execute(data.getFechaInicio(), data.getFechaFinal());
    }
}
