package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.actualizarambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.ActualizarAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.ActualizarAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.rules.ActualizarAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.rules.ActualizarAmbienteNameDoesNotExistRule;

@Service
public final class ActualizarAmbienteRuleValidatorImpl implements ActualizarAmbienteRuleValidator {
    private final ActualizarAmbienteExistsRule rule1;
    private final ActualizarAmbienteNameDoesNotExistRule rule2;

    public ActualizarAmbienteRuleValidatorImpl(final ActualizarAmbienteExistsRule rule1, final ActualizarAmbienteNameDoesNotExistRule rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
    }

    @Override
    public void validate(final ActualizarAmbienteDomain data) {
        rule1.execute(data);
        rule2.execute(data);
    }
}

