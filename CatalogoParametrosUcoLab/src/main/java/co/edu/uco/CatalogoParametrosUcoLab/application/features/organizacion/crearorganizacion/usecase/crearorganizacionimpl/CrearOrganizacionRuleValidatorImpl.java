package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.crearorganizacionimpl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.RangoFechasIsValidRule;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNameIsNotNullRule;

@Service
public class CrearOrganizacionRuleValidatorImpl implements CrearOrganizacionRuleValidator {

    private final OrganizacionNameIsNotNullRule organizacionNameIsNotNullRule;
    private final OrganizacionNameIsNotEmptyRule organizacionNameIsNotEmptyRule;
    private final OrganizacionNameDoesNotExistRule organizacionNameDoesNotExistRule;

    public CrearOrganizacionRuleValidatorImpl(final OrganizacionNameIsNotNullRule organizacionNameIsNotNullRule,
                                               final OrganizacionNameIsNotEmptyRule organizacionNameIsNotEmptyRule,
                                               final OrganizacionNameDoesNotExistRule organizacionNameDoesNotExistRule) {
        this.organizacionNameIsNotNullRule = organizacionNameIsNotNullRule;
        this.organizacionNameIsNotEmptyRule = organizacionNameIsNotEmptyRule;
        this.organizacionNameDoesNotExistRule = organizacionNameDoesNotExistRule;
    }

    @Override
    public void validate(final CrearOrganizacionDomain data) {
        organizacionNameIsNotNullRule.execute(data);
        organizacionNameIsNotEmptyRule.execute(data);
        organizacionNameDoesNotExistRule.execute(data);
        RangoFechasIsValidRule.execute(data.getFechaInicio(), data.getFechaFinal());
    }
}
