package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.actualizarambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.ActualizarAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.ActualizarAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.rules.ActualizarAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.rules.ActualizarAmbienteNameDoesNotExistRule;

@Service
public final class ActualizarAmbienteRuleValidatorImpl implements ActualizarAmbienteRuleValidator {
    private final ActualizarAmbienteExistsRule ambienteExistsRule;
    private final ActualizarAmbienteNameDoesNotExistRule ambienteNameDoesNotExistRule;

    public ActualizarAmbienteRuleValidatorImpl(final ActualizarAmbienteExistsRule ambienteExistsRule, final ActualizarAmbienteNameDoesNotExistRule ambienteNameDoesNotExistRule) {
        this.ambienteExistsRule = ambienteExistsRule;
        this.ambienteNameDoesNotExistRule = ambienteNameDoesNotExistRule;
    }

    @Override
    public void validate(final ActualizarAmbienteDomain data) {
        ambienteExistsRule.execute(data);
        ambienteNameDoesNotExistRule.execute(data);
    }
}

