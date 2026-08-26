package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.actualizarmetadatoimpl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.ActualizarMetadatoRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.domain.ActualizarMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
@Service
public final class ActualizarMetadatoRuleValidatorImpl implements ActualizarMetadatoRuleValidator {
    private final MetadatoRepository metadatos; private final ParametroRepository parametros; private final TipoMetadatoRepository tipos;
    public ActualizarMetadatoRuleValidatorImpl(final MetadatoRepository m, final ParametroRepository p, final TipoMetadatoRepository t) {
        metadatos = m; parametros = p; tipos = t;
    }
    @Override public void validate(final ActualizarMetadatoDomain data) {
        if (metadatos.findById(data.getId()).isEmpty()) throw NotFoundException.build("El metadato no existe.");
        if (parametros.findById(data.getIdParametro()).isEmpty()) throw NotFoundException.build("El parametro asociado al metadato no existe.");
        if (tipos.findById(data.getIdTipoMetadato()).isEmpty()) throw NotFoundException.build("El tipo de metadato no existe.");
        if (TextHelper.isBlank(data.getValor())) throw ValidationException.build("El valor del metadato es obligatorio.");
    }
}
