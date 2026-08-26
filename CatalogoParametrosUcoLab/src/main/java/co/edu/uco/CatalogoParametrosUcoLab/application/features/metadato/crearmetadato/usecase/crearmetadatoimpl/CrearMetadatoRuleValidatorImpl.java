package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.crearmetadatoimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.CrearMetadatoRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.CrearMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.*;

@Service
public final class CrearMetadatoRuleValidatorImpl implements CrearMetadatoRuleValidator {
    private final MetadatoValorIsNotEmptyRule valorRule;
    private final MetadatoParametroExistsRule parametroRule;
    private final MetadatoTipoMetadatoExistsRule tipoRule;
    public CrearMetadatoRuleValidatorImpl(final MetadatoValorIsNotEmptyRule valorRule,
            final MetadatoParametroExistsRule parametroRule, final MetadatoTipoMetadatoExistsRule tipoRule) {
        this.valorRule = valorRule; this.parametroRule = parametroRule; this.tipoRule = tipoRule;
    }
    @Override public void validate(final CrearMetadatoDomain data) {
        valorRule.execute(data); parametroRule.execute(data); tipoRule.execute(data);
    }
}
