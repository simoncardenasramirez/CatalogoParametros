package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.actualizarmetadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.ActualizarMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.ActualizarMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.ActualizarMetadatoAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.ActualizarMetadatoAmbienteParametroExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.ActualizarMetadatoAmbienteAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.ActualizarMetadatoAmbienteEstadoExistsRule;

@Service
public final class ActualizarMetadatoAmbienteRuleValidatorImpl implements ActualizarMetadatoAmbienteRuleValidator {
    private final ActualizarMetadatoAmbienteExistsRule rule1;
    private final ActualizarMetadatoAmbienteParametroExistsRule rule2;
    private final ActualizarMetadatoAmbienteAmbienteExistsRule rule3;
    private final ActualizarMetadatoAmbienteEstadoExistsRule rule4;

    public ActualizarMetadatoAmbienteRuleValidatorImpl(final ActualizarMetadatoAmbienteExistsRule rule1, final ActualizarMetadatoAmbienteParametroExistsRule rule2, final ActualizarMetadatoAmbienteAmbienteExistsRule rule3, final ActualizarMetadatoAmbienteEstadoExistsRule rule4) {
        this.rule1 = rule1;
        this.rule2 = rule2;
        this.rule3 = rule3;
        this.rule4 = rule4;
    }

    @Override
    public void validate(final ActualizarMetadatoAmbienteDomain data) {
        rule1.execute(data);
        rule2.execute(data);
        rule3.execute(data);
        rule4.execute(data);
    }
}

