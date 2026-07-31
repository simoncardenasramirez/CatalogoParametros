package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.crearorganizacionimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNombreIsNotNullRule;

@Service
public class CrearOrganizacionRuleValidatorImpl implements CrearOrganizacionRuleValidator {

    private final OrganizacionNombreIsNotNullRule organizacionNombreIsNotNullRule;
    private final OrganizacionNombreIsNotEmptyRule organizacionNombreIsNotEmptyRule;
    private final OrganizacionNombreDoesNotExistRule organizacionNombreDoesNotExistRule;

    public CrearOrganizacionRuleValidatorImpl(final OrganizacionNombreIsNotNullRule organizacionNombreIsNotNullRule,
                                               final OrganizacionNombreIsNotEmptyRule organizacionNombreIsNotEmptyRule,
                                               final OrganizacionNombreDoesNotExistRule organizacionNombreDoesNotExistRule) {
        this.organizacionNombreIsNotNullRule = organizacionNombreIsNotNullRule;
        this.organizacionNombreIsNotEmptyRule = organizacionNombreIsNotEmptyRule;
        this.organizacionNombreDoesNotExistRule = organizacionNombreDoesNotExistRule;
    }

    @Override
    public void validate(final CrearOrganizacionDomain data) {
        organizacionNombreIsNotNullRule.execute(data);
        organizacionNombreIsNotEmptyRule.execute(data);
        organizacionNombreDoesNotExistRule.execute(data);
    }
}
