package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.crearestadometadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.CrearEstadoMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.CrearEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.rules.EstadoMetadatoAmbienteNameDoesNotExistRule;

@Service
public final class CrearEstadoMetadatoAmbienteRuleValidatorImpl implements CrearEstadoMetadatoAmbienteRuleValidator {
    private final EstadoMetadatoAmbienteNameDoesNotExistRule rule1;

    public CrearEstadoMetadatoAmbienteRuleValidatorImpl(final EstadoMetadatoAmbienteNameDoesNotExistRule rule1) {
        this.rule1 = rule1;
    }

    @Override
    public void validate(final CrearEstadoMetadatoAmbienteDomain data) {
        rule1.execute(data);
    }
}

