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
    private final ActualizarMetadatoAmbienteExistsRule metadatoAmbienteExistsRule;
    private final ActualizarMetadatoAmbienteParametroExistsRule parametroExistsRule;
    private final ActualizarMetadatoAmbienteAmbienteExistsRule ambienteExistsRule;
    private final ActualizarMetadatoAmbienteEstadoExistsRule estadoMetadatoAmbienteExistsRule;

    public ActualizarMetadatoAmbienteRuleValidatorImpl(final ActualizarMetadatoAmbienteExistsRule metadatoAmbienteExistsRule, final ActualizarMetadatoAmbienteParametroExistsRule parametroExistsRule, final ActualizarMetadatoAmbienteAmbienteExistsRule ambienteExistsRule, final ActualizarMetadatoAmbienteEstadoExistsRule estadoMetadatoAmbienteExistsRule) {
        this.metadatoAmbienteExistsRule = metadatoAmbienteExistsRule;
        this.parametroExistsRule = parametroExistsRule;
        this.ambienteExistsRule = ambienteExistsRule;
        this.estadoMetadatoAmbienteExistsRule = estadoMetadatoAmbienteExistsRule;
    }

    @Override
    public void validate(final ActualizarMetadatoAmbienteDomain data) {
        metadatoAmbienteExistsRule.execute(data);
        parametroExistsRule.execute(data);
        ambienteExistsRule.execute(data);
        estadoMetadatoAmbienteExistsRule.execute(data);
    }
}

