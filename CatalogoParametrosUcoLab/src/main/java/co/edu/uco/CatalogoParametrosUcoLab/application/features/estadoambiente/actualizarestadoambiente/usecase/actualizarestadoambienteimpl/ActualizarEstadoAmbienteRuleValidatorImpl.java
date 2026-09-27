package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.actualizarestadoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.ActualizarEstadoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.ActualizarEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.rules.ActualizarEstadoAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.rules.ActualizarEstadoAmbienteNameDoesNotExistRule;

@Service
public final class ActualizarEstadoAmbienteRuleValidatorImpl implements ActualizarEstadoAmbienteRuleValidator {
    private final ActualizarEstadoAmbienteExistsRule rule1;
    private final ActualizarEstadoAmbienteNameDoesNotExistRule rule2;

    public ActualizarEstadoAmbienteRuleValidatorImpl(final ActualizarEstadoAmbienteExistsRule rule1, final ActualizarEstadoAmbienteNameDoesNotExistRule rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
    }

    @Override
    public void validate(final ActualizarEstadoAmbienteDomain data) {
        rule1.execute(data);
        rule2.execute(data);
    }
}

