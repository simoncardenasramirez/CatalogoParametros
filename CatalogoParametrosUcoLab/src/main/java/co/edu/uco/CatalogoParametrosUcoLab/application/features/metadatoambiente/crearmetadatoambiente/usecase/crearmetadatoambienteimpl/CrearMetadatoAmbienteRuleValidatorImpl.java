package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.crearmetadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.CrearMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.CrearMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.MetadatoAmbienteParametroExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.MetadatoAmbienteAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.MetadatoAmbienteEstadoExistsRule;

@Service
public final class CrearMetadatoAmbienteRuleValidatorImpl implements CrearMetadatoAmbienteRuleValidator {
    private final MetadatoAmbienteParametroExistsRule parametroExistsRule;
    private final MetadatoAmbienteAmbienteExistsRule ambienteExistsRule;
    private final MetadatoAmbienteEstadoExistsRule estadoMetadatoAmbienteExistsRule;

    public CrearMetadatoAmbienteRuleValidatorImpl(final MetadatoAmbienteParametroExistsRule parametroExistsRule, final MetadatoAmbienteAmbienteExistsRule ambienteExistsRule, final MetadatoAmbienteEstadoExistsRule estadoMetadatoAmbienteExistsRule) {
        this.parametroExistsRule = parametroExistsRule;
        this.ambienteExistsRule = ambienteExistsRule;
        this.estadoMetadatoAmbienteExistsRule = estadoMetadatoAmbienteExistsRule;
    }

    @Override
    public void validate(final CrearMetadatoAmbienteDomain data) {
        parametroExistsRule.execute(data);
        ambienteExistsRule.execute(data);
        estadoMetadatoAmbienteExistsRule.execute(data);
    }
}

