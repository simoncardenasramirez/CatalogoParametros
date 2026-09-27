package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.actualizarestadometadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.ActualizarEstadoMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.ActualizarEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.rules.ActualizarEstadoMetadatoAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.rules.ActualizarEstadoMetadatoAmbienteNameDoesNotExistRule;

@Service
public final class ActualizarEstadoMetadatoAmbienteRuleValidatorImpl implements ActualizarEstadoMetadatoAmbienteRuleValidator {
    private final ActualizarEstadoMetadatoAmbienteExistsRule rule1;
    private final ActualizarEstadoMetadatoAmbienteNameDoesNotExistRule rule2;

    public ActualizarEstadoMetadatoAmbienteRuleValidatorImpl(final ActualizarEstadoMetadatoAmbienteExistsRule rule1, final ActualizarEstadoMetadatoAmbienteNameDoesNotExistRule rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
    }

    @Override
    public void validate(final ActualizarEstadoMetadatoAmbienteDomain data) {
        rule1.execute(data);
        rule2.execute(data);
    }
}

