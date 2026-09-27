package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.crearambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.CrearAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.CrearAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.rules.AmbienteNameDoesNotExistRule;

@Service
public final class CrearAmbienteRuleValidatorImpl implements CrearAmbienteRuleValidator {
    private final AmbienteNameDoesNotExistRule rule1;

    public CrearAmbienteRuleValidatorImpl(final AmbienteNameDoesNotExistRule rule1) {
        this.rule1 = rule1;
    }

    @Override
    public void validate(final CrearAmbienteDomain data) {
        rule1.execute(data);
    }
}

