package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameFormatIsValidRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class ParametroNameFormatIsValidRuleImpl implements ParametroNameFormatIsValidRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private static final String VALID_NAME_PATTERN = "^[A-Za-z0-9_.-]+$";

    @Override
    public void execute(final CrearParametroDomain data) {
        if (!data.getNombre().matches(VALID_NAME_PATTERN)) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-133"));
        }
    }
}
