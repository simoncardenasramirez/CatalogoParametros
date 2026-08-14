package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameDoesNotExistRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class ParametroNameDoesNotExistRuleImpl implements ParametroNameDoesNotExistRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final ParametroRepository parametroRepository;

    public ParametroNameDoesNotExistRuleImpl(final ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public void execute(final CrearParametroDomain data) {
        if (parametroRepository.existsByNombre(data.getNombre())) {
            throw ConflictException.build(consultarMensajePort.consultarMensaje("MSG-132"));
        }
    }
}
