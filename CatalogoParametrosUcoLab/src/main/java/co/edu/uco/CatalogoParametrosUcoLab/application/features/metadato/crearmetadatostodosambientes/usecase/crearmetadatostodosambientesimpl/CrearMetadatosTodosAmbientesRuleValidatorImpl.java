package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.crearmetadatostodosambientesimpl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.CrearMetadatosTodosAmbientesRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.domain.CrearMetadatosTodosAmbientesDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.CrearMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.MetadatoParametroExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.MetadatoTipoMetadatoExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.MetadatoValorIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearMetadatosTodosAmbientesRuleValidatorImpl implements CrearMetadatosTodosAmbientesRuleValidator {
    private final MetadatoValorIsNotEmptyRule valorRule;
    private final MetadatoParametroExistsRule parametroRule;
    private final MetadatoTipoMetadatoExistsRule tipoRule;
    private final AmbienteRepository ambientes;

    public CrearMetadatosTodosAmbientesRuleValidatorImpl(final MetadatoValorIsNotEmptyRule valorRule,
            final MetadatoParametroExistsRule parametroRule, final MetadatoTipoMetadatoExistsRule tipoRule,
            final AmbienteRepository ambientes) {
        this.valorRule = valorRule; this.parametroRule = parametroRule; this.tipoRule = tipoRule; this.ambientes = ambientes;
    }

    @Override public void validate(final CrearMetadatosTodosAmbientesDomain data) {
        var base = CrearMetadatoDomain.create(UUIDHelper.getDefault(), data.getIdParametro(), data.getIdTipoMetadato(), null, data.getValor());
        valorRule.execute(base); parametroRule.execute(base); tipoRule.execute(base);
        if (ambientes.findAll().isEmpty()) throw NotFoundException.build("No hay ambientes disponibles para asociar el metadato.");
    }
}
