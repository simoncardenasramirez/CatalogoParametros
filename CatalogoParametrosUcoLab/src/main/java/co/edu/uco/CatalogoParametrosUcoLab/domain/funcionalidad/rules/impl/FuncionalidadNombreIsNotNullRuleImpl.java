package co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.FuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.FuncionalidadNombreIsNotNullRule;

@Service
public final class FuncionalidadNombreIsNotNullRuleImpl implements FuncionalidadNombreIsNotNullRule {

    @Override
    public void execute(final FuncionalidadDomain data) {
        if (data == null || data.getNombre() == null) {
            throw new FuncionalidadException("El nombre de la funcionalidad es obligatorio.");
        }
    }
}