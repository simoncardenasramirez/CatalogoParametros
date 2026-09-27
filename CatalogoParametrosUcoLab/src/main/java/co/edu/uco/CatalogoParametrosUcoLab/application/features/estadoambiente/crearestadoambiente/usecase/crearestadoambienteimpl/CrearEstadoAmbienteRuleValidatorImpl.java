package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.crearestadoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.CrearEstadoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.CrearEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.rules.EstadoAmbienteNameDoesNotExistRule;

@Service
public final class CrearEstadoAmbienteRuleValidatorImpl implements CrearEstadoAmbienteRuleValidator {
    private final EstadoAmbienteNameDoesNotExistRule rule1;

    public CrearEstadoAmbienteRuleValidatorImpl(final EstadoAmbienteNameDoesNotExistRule rule1) {
        this.rule1 = rule1;
    }

    @Override
    public void validate(final CrearEstadoAmbienteDomain data) {
        rule1.execute(data);
    }
}

