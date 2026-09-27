package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.crearaplicacionimpl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.RangoFechasIsValidRule;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionOrganizacionExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacionRuleValidator;

@Service
public class CrearAplicacionRuleValidatorImpl implements CrearAplicacionRuleValidator {

    private final AplicacionNameIsNotNullRule aplicacionNameIsNotNullRule;
    private final AplicacionNameIsNotEmptyRule aplicacionNameIsNotEmptyRule;
    private final AplicacionNameDoesNotExistRule aplicacionNameDoesNotExistRule;
    private final AplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule;

    public CrearAplicacionRuleValidatorImpl(final AplicacionNameIsNotNullRule aplicacionNameIsNotNullRule,
                                               final AplicacionNameIsNotEmptyRule aplicacionNameIsNotEmptyRule,
                                               final AplicacionNameDoesNotExistRule aplicacionNameDoesNotExistRule,
                                               final AplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule) {
        this.aplicacionNameIsNotNullRule = aplicacionNameIsNotNullRule;
        this.aplicacionNameIsNotEmptyRule = aplicacionNameIsNotEmptyRule;
        this.aplicacionNameDoesNotExistRule = aplicacionNameDoesNotExistRule;
        this.aplicacionOrganizacionExistsRule = aplicacionOrganizacionExistsRule;
    }

    @Override
    public void validate(final CrearAplicacionDomain data) {
        aplicacionNameIsNotNullRule.execute(data);
        aplicacionNameIsNotEmptyRule.execute(data);
        aplicacionNameDoesNotExistRule.execute(data);
        aplicacionOrganizacionExistsRule.execute(data);
        RangoFechasIsValidRule.execute(data.getFechaInicio(), data.getFechaFinal());
    }
}
