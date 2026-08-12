package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class AplicacionNombreIsNotEmptyRuleImpl implements AplicacionNombreIsNotEmptyRule {

    @Override
    public void execute(final CrearAplicacionDomain data) {
        if (data == null || TextHelper.isBlank(data.getNombre())) {
            throw ValidationException.build("El nombre de la aplicacion no puede estar vacio.");
        }
    }
}
