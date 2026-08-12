package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNombreIsNotNullRule;

@Service
public final class FuncionalidadNombreIsNotNullRuleImpl implements FuncionalidadNombreIsNotNullRule {

    @Override
    public void execute(final CrearFuncionalidadDomain data) {
        if (data == null || data.getNombre() == null) {
            throw ValidationException.build("El nombre de la funcionalidad es obligatorio.");
        }
    }
}