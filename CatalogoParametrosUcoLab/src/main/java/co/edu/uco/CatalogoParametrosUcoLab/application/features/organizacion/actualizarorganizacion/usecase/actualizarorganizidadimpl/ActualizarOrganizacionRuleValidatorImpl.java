package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.actualizarorganizidadimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.ActualizarOrganizacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;

@Service
public class ActualizarOrganizacionRuleValidatorImpl implements ActualizarOrganizacionRuleValidator {

    private final List<String> messages = new ArrayList<>();
    private final ActualizarOrganizacionIdExistsRule idExistsRule;
    private final ActualizarOrganizacionNombreIsNotNullRule nombreIsNotNullRule;
    private final ActualizarOrganizacionNombreIsNotEmptyRule nombreIsNotEmptyRule;
    private final ActualizarOrganizacionNombreDoesNotExistRule nombreDoesNotExistRule;

    public ActualizarOrganizacionRuleValidatorImpl(final ActualizarOrganizacionIdExistsRule idExistsRule,
            final ActualizarOrganizacionNombreIsNotNullRule nombreIsNotNullRule,
            final ActualizarOrganizacionNombreIsNotEmptyRule nombreIsNotEmptyRule,
            final ActualizarOrganizacionNombreDoesNotExistRule nombreDoesNotExistRule) {
        this.idExistsRule = idExistsRule;
        this.nombreIsNotNullRule = nombreIsNotNullRule;
        this.nombreIsNotEmptyRule = nombreIsNotEmptyRule;
        this.nombreDoesNotExistRule = nombreDoesNotExistRule;
    }

    @Override
    public void validate(final ActualizarOrganizacionDomain data) {
        messages.clear();
        try {
            idExistsRule.execute(data);
        } catch (final Exception e) {
            messages.add(e.getMessage());
        }
        try {
            nombreIsNotNullRule.execute(data);
        } catch (final Exception e) {
            messages.add(e.getMessage());
        }
        try {
            nombreIsNotEmptyRule.execute(data);
        } catch (final Exception e) {
            messages.add(e.getMessage());
        }
        try {
            nombreDoesNotExistRule.execute(data);
        } catch (final Exception e) {
            messages.add(e.getMessage());
        }
        if (!messages.isEmpty()) {
            throw ValidationException.build(String.join(", ", messages));
        }
    }
}
