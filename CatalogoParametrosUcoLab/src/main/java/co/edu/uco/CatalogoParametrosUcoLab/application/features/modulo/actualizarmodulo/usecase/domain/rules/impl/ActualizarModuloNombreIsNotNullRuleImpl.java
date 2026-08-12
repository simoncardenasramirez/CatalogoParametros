package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class ActualizarModuloNombreIsNotNullRuleImpl implements ActualizarModuloNombreIsNotNullRule {

    @Override
    public void execute(final ActualizarModuloDomain data) {
        if (data.getNombre() == null) {
            throw ValidationException.build(
                    "El nombre del modulo no puede ser nulo.");
        }
    }
}
