package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class ActualizarFuncionalidadNombreIsNotEmptyRuleImpl implements ActualizarFuncionalidadNombreIsNotEmptyRule {

    @Override
    public void execute(final ActualizarFuncionalidadDomain data) {
        if (data == null || data.getNombre() == null || data.getNombre().isEmpty()) {
            throw ValidationException.build("El nombre de la funcionalidad no puede estar vacío.");
        }
    }
}