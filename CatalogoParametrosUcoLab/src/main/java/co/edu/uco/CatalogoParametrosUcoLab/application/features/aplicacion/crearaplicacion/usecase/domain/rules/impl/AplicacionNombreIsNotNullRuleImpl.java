package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.exception.AplicacionException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNombreIsNotNullRule;

@Service
public final class AplicacionNombreIsNotNullRuleImpl implements AplicacionNombreIsNotNullRule {

    @Override
    public void execute(final CrearAplicacionDomain data) {
        if (data == null || data.getNombre() == null) {
            throw new AplicacionException("El nombre de la aplicacion es obligatorio.");
        }
    }
}
