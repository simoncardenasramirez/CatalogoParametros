package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.ModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.exception.ModuloException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreIsNotEmptyRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

@Service
public final class ModuloNombreIsNotEmptyRuleImpl implements ModuloNombreIsNotEmptyRule {

    @Override
    public void execute(final ModuloDomain data) {
        if (TextHelper.isBlank(data.getNombre())) {
            throw new ModuloException("El nombre del modulo no puede estar vacio.");
        }
    }
}
