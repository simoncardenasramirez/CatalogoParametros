package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.actualizarorganizidadimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.RangoFechasIsValidRule;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.ActualizarOrganizacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNameIsNotNullRule;

@Service
public class ActualizarOrganizacionRuleValidatorImpl implements ActualizarOrganizacionRuleValidator {

    private final List<String> messages = new ArrayList<>();
    private final ActualizarOrganizacionIdExistsRule idExistsRule;
    private final ActualizarOrganizacionNameIsNotNullRule nombreIsNotNullRule;
    private final ActualizarOrganizacionNameIsNotEmptyRule nombreIsNotEmptyRule;
    private final ActualizarOrganizacionNameDoesNotExistRule nombreDoesNotExistRule;

    public ActualizarOrganizacionRuleValidatorImpl(final ActualizarOrganizacionIdExistsRule idExistsRule,
                                                   final ActualizarOrganizacionNameIsNotNullRule nombreIsNotNullRule,
                                                   final ActualizarOrganizacionNameIsNotEmptyRule nombreIsNotEmptyRule,
                                                   final ActualizarOrganizacionNameDoesNotExistRule nombreDoesNotExistRule) {
        this.idExistsRule = idExistsRule;
        this.nombreIsNotNullRule = nombreIsNotNullRule;
        this.nombreIsNotEmptyRule = nombreIsNotEmptyRule;
        this.nombreDoesNotExistRule = nombreDoesNotExistRule;
    }

    @Override
    public void validate(final ActualizarOrganizacionDomain data) {
        idExistsRule.execute(data);
        nombreIsNotNullRule.execute(data);
        nombreIsNotEmptyRule.execute(data);
        nombreDoesNotExistRule.execute(data);
        RangoFechasIsValidRule.execute(data.getFechaInicio(), data.getFechaFinal());
    }
}
