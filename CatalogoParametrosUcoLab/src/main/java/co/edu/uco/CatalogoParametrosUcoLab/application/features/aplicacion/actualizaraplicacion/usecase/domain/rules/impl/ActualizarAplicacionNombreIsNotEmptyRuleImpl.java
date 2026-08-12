package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class ActualizarAplicacionNombreIsNotEmptyRuleImpl implements ActualizarAplicacionNombreIsNotEmptyRule {

    @Override
    public void execute(final ActualizarAplicacionDomain data) {
        if (data == null || data.getNombre() == null || data.getNombre().isEmpty()) {
            throw ValidationException.build("El nombre de la aplicacion no puede estar vacio.");
        }
    }
}
