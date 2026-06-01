package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.exception.ModuloException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreIsNotNullRule;
import org.springframework.stereotype.Service;

@Service
public final class ModuloNombreIsNotNullRuleImpl implements ModuloNombreIsNotNullRule {

    @Override
    public void execute(final CrearModuloDomain data) {
        if (data == null || data.getNombre() == null) {
            throw new ModuloException("El nombre del modulo es obligatorio.");
        }
    }
}