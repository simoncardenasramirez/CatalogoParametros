package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNombreIsNotNullRule;
import org.springframework.stereotype.Service;

@Service
public final class ActualizarParametroNombreIsNotNullRuleImpl implements ActualizarParametroNombreIsNotNullRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    @Override
    public void execute(final ActualizarParametroDomain data) {
        if (data == null || data.getNombre() == null) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-120"));
        }
    }
}
