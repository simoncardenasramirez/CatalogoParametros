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
    private final MetadatoAmbienteExistsRule ambienteRule;
    public CrearMetadatoRuleValidatorImpl(final MetadatoValorIsNotEmptyRule valorRule,
            final MetadatoParametroExistsRule parametroRule, final MetadatoTipoMetadatoExistsRule tipoRule,
            final MetadatoAmbienteExistsRule ambienteRule) {
        this.valorRule = valorRule; this.parametroRule = parametroRule; this.tipoRule = tipoRule;
        this.ambienteRule = ambienteRule;
    }
    @Override public void validate(final CrearMetadatoDomain data) {
        ambienteRule.execute(data.getIdAmbiente());
        valorRule.execute(data); parametroRule.execute(data); tipoRule.execute(data);
    }
}
