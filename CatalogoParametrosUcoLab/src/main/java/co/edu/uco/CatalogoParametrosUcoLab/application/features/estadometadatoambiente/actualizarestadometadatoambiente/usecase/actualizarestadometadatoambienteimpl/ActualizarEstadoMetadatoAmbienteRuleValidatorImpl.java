package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.actualizarestadometadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.ActualizarEstadoMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.ActualizarEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.rules.ActualizarEstadoMetadatoAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.rules.ActualizarEstadoMetadatoAmbienteNameDoesNotExistRule;

@Service
public final class ActualizarEstadoMetadatoAmbienteRuleValidatorImpl implements ActualizarEstadoMetadatoAmbienteRuleValidator {
    private final ActualizarEstadoMetadatoAmbienteExistsRule estadoMetadatoAmbienteExistsRule;
    private final ActualizarEstadoMetadatoAmbienteNameDoesNotExistRule estadoMetadatoAmbienteNameDoesNotExistRule;

    public ActualizarEstadoMetadatoAmbienteRuleValidatorImpl(final ActualizarEstadoMetadatoAmbienteExistsRule estadoMetadatoAmbienteExistsRule, final ActualizarEstadoMetadatoAmbienteNameDoesNotExistRule estadoMetadatoAmbienteNameDoesNotExistRule) {
        this.estadoMetadatoAmbienteExistsRule = estadoMetadatoAmbienteExistsRule;
        this.estadoMetadatoAmbienteNameDoesNotExistRule = estadoMetadatoAmbienteNameDoesNotExistRule;
    }

    @Override
    public void validate(final ActualizarEstadoMetadatoAmbienteDomain data) {
        estadoMetadatoAmbienteExistsRule.execute(data);
        estadoMetadatoAmbienteNameDoesNotExistRule.execute(data);
    }
}

