package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.actualizarmetadatoimpl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.ActualizarMetadatoRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.domain.ActualizarMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.MetadatoValorTypeRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.MetadatoAmbienteExistsRule;
@Service
public final class ActualizarMetadatoRuleValidatorImpl implements ActualizarMetadatoRuleValidator {
    private final MetadatoRepository metadatos; private final ParametroRepository parametros; private final TipoMetadatoRepository tipos;
    private final MetadatoAmbienteExistsRule ambienteRule;
    public ActualizarMetadatoRuleValidatorImpl(final MetadatoRepository m, final ParametroRepository p, final TipoMetadatoRepository t,
            final MetadatoAmbienteExistsRule ambienteRule) {
        metadatos = m; parametros = p; tipos = t;
        this.ambienteRule = ambienteRule;
    }
    @Override public void validate(final ActualizarMetadatoDomain data) {
        if (metadatos.findById(data.getId()).isEmpty()) throw NotFoundException.build("El metadato no existe.");
        ambienteRule.execute(data.getIdAmbiente());
        if (parametros.findById(data.getIdParametro()).isEmpty()) throw NotFoundException.build("El parametro asociado al metadato no existe.");
        var tipo = tipos.findById(data.getIdTipoMetadato())
                .orElseThrow(() -> NotFoundException.build("El tipo de metadato no existe."));
        MetadatoValorTypeRule.execute(tipo.getTipo(), data.getValor());
    }
}
