package co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.FuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.rules.FuncionalidadNombreIsNotEmptyRule;

@Service
public final class FuncionalidadNombreIsNotEmptyRuleImpl implements FuncionalidadNombreIsNotEmptyRule {

    @Override
    public void execute(final FuncionalidadDomain data) {
        if (data == null || TextHelper.isBlank(data.getNombre())) {
            throw new FuncionalidadException("El nombre de la funcionalidad no puede estar vacio.");
        }
    }
}