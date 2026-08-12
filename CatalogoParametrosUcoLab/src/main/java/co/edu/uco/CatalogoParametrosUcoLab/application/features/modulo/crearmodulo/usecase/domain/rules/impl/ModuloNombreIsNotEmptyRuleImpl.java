package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreIsNotEmptyRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

@Service
public final class ModuloNombreIsNotEmptyRuleImpl implements ModuloNombreIsNotEmptyRule {

    @Override
    public void execute(final CrearModuloDomain data) {
        if (TextHelper.isBlank(data.getNombre())) {
            throw ValidationException.build("El nombre del modulo no puede estar vacio.");
        }
    }
}
