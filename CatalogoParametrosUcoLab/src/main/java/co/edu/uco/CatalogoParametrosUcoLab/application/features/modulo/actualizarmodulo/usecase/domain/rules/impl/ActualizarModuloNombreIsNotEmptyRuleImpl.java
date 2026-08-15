package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

@Service
public final class ActualizarModuloNombreIsNotEmptyRuleImpl implements ActualizarModuloNombreIsNotEmptyRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    @Override
    public void execute(final ActualizarModuloDomain data) {
        final var nombre = TextHelper.applyTrim(data.getNombre());
        if (TextHelper.isBlank(nombre)) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-76"));
        }
    }
}
