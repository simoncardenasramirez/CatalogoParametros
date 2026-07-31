package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException;

@Service
public final class ActualizarFuncionalidadNombreIsNotNullRuleImpl implements ActualizarFuncionalidadNombreIsNotNullRule {

    @Override
    public void execute(final ActualizarFuncionalidadDomain data) {
        if (data == null || data.getNombre() == null) {
            throw new FuncionalidadException("El nombre de la funcionalidad es obligatorio.");
        }
    }
}