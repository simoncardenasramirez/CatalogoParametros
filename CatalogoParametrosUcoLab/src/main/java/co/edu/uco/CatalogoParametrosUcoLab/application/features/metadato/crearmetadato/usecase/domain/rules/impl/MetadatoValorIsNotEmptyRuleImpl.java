package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.CrearMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.MetadatoValorIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class MetadatoValorIsNotEmptyRuleImpl implements MetadatoValorIsNotEmptyRule {
    @Override public void execute(final CrearMetadatoDomain data) {
        if (data.getValor() == null || data.getValor().isNull() || data.getValor().isMissingNode()
                || data.getValor().isTextual() && data.getValor().asText().isBlank()) {
            throw ValidationException.build("El valor del metadato es obligatorio.");
        }
    }
}
