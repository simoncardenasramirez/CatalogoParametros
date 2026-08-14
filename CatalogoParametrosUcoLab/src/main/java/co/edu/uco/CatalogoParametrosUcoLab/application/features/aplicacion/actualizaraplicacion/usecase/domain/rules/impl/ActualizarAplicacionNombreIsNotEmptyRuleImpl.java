package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class ActualizarAplicacionNombreIsNotEmptyRuleImpl implements ActualizarAplicacionNombreIsNotEmptyRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    @Override
    public void execute(final ActualizarAplicacionDomain data) {
        if (data == null || data.getNombre() == null || data.getNombre().isEmpty()) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-13"));
        }
    }
}
