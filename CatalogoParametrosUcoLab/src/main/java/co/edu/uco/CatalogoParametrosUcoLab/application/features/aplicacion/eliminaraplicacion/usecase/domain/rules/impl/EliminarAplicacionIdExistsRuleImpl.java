package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.EliminarAplicacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarAplicacionIdExistsRuleImpl implements EliminarAplicacionIdExistsRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final AplicacionRepository aplicacionRepository;

    public EliminarAplicacionIdExistsRuleImpl(final AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    @Override
    public void execute(final UUID id) {
        if (aplicacionRepository.findById(id).isEmpty()) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-27"));
        }
    }
}
