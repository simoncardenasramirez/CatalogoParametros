package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.exception.AplicacionException;

@Service
public final class ActualizarAplicacionNombreIsNotNullRuleImpl implements ActualizarAplicacionNombreIsNotNullRule {

    @Override
    public void execute(final ActualizarAplicacionDomain data) {
        if (data == null || data.getNombre() == null) {
            throw new AplicacionException("El nombre de la aplicacion es obligatorio.");
        }
    }
}
