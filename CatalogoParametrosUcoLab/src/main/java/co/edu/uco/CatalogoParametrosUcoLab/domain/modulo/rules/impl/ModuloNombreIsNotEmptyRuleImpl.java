package co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.ModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.exception.ModuloException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.rules.ModuloNombreIsNotEmptyRule;

@Service
public final class ModuloNombreIsNotEmptyRuleImpl implements ModuloNombreIsNotEmptyRule {

    @Override
    public void execute(final ModuloDomain data) {
        if (TextHelper.isBlank(data.getNombre())) {
            throw new ModuloException("El nombre del modulo no puede estar vacio.");
        }
    }
}
