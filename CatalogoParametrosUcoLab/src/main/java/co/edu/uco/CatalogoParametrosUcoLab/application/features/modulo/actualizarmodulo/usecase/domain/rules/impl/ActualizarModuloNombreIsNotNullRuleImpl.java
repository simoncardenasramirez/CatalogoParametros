package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreIsNotNullRule;

@Service
public final class ActualizarModuloNombreIsNotNullRuleImpl implements ActualizarModuloNombreIsNotNullRule {

    @Override
    public void execute(final ActualizarModuloDomain data) {
        if (data.getNombre() == null) {
            throw new co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.exception.ModuloException(
                    "El nombre del modulo no puede ser nulo.");
        }
    }
}
