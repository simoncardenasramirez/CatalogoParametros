package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.crearestadometadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.CrearEstadoMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.CrearEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.rules.EstadoMetadatoAmbienteNameDoesNotExistRule;

@Service
public final class CrearEstadoMetadatoAmbienteRuleValidatorImpl implements CrearEstadoMetadatoAmbienteRuleValidator {
    private final EstadoMetadatoAmbienteNameDoesNotExistRule estadoMetadatoAmbienteNameDoesNotExistRule;

    public CrearEstadoMetadatoAmbienteRuleValidatorImpl(final EstadoMetadatoAmbienteNameDoesNotExistRule estadoMetadatoAmbienteNameDoesNotExistRule) {
        this.estadoMetadatoAmbienteNameDoesNotExistRule = estadoMetadatoAmbienteNameDoesNotExistRule;
    }

    @Override
    public void validate(final CrearEstadoMetadatoAmbienteDomain data) {
        estadoMetadatoAmbienteNameDoesNotExistRule.execute(data);
    }
}

