package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.actualizarestadoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.ActualizarEstadoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.ActualizarEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.rules.ActualizarEstadoAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.rules.ActualizarEstadoAmbienteNameDoesNotExistRule;

@Service
public final class ActualizarEstadoAmbienteRuleValidatorImpl implements ActualizarEstadoAmbienteRuleValidator {
    private final ActualizarEstadoAmbienteExistsRule estadoAmbienteExistsRule;
    private final ActualizarEstadoAmbienteNameDoesNotExistRule estadoAmbienteNameDoesNotExistRule;

    public ActualizarEstadoAmbienteRuleValidatorImpl(final ActualizarEstadoAmbienteExistsRule estadoAmbienteExistsRule, final ActualizarEstadoAmbienteNameDoesNotExistRule estadoAmbienteNameDoesNotExistRule) {
        this.estadoAmbienteExistsRule = estadoAmbienteExistsRule;
        this.estadoAmbienteNameDoesNotExistRule = estadoAmbienteNameDoesNotExistRule;
    }

    @Override
    public void validate(final ActualizarEstadoAmbienteDomain data) {
        estadoAmbienteExistsRule.execute(data);
        estadoAmbienteNameDoesNotExistRule.execute(data);
    }
}

