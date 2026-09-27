package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.crearmetadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.CrearMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.CrearMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.MetadatoAmbienteParametroExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.MetadatoAmbienteAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.MetadatoAmbienteEstadoExistsRule;

@Service
public final class CrearMetadatoAmbienteRuleValidatorImpl implements CrearMetadatoAmbienteRuleValidator {
    private final MetadatoAmbienteParametroExistsRule rule1;
    private final MetadatoAmbienteAmbienteExistsRule rule2;
    private final MetadatoAmbienteEstadoExistsRule rule3;

    public CrearMetadatoAmbienteRuleValidatorImpl(final MetadatoAmbienteParametroExistsRule rule1, final MetadatoAmbienteAmbienteExistsRule rule2, final MetadatoAmbienteEstadoExistsRule rule3) {
        this.rule1 = rule1;
        this.rule2 = rule2;
        this.rule3 = rule3;
    }

    @Override
    public void validate(final CrearMetadatoAmbienteDomain data) {
        rule1.execute(data);
        rule2.execute(data);
        rule3.execute(data);
    }
}

