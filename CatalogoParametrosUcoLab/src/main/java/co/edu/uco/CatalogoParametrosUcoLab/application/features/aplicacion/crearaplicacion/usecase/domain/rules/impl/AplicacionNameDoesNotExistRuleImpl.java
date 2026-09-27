package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class AplicacionNameDoesNotExistRuleImpl implements AplicacionNameDoesNotExistRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final AplicacionRepository aplicacionRepository;

    public AplicacionNameDoesNotExistRuleImpl(final AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    @Override
    public void execute(final CrearAplicacionDomain data) {
        if (aplicacionRepository.existsByNombre(data.getNombre())) {
            throw ConflictException.build(consultarMensajePort.consultarMensaje("MSG-23"));
        }
    }
}
