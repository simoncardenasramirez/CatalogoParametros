package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class ActualizarAplicacionNombreDoesNotExistRuleImpl implements ActualizarAplicacionNombreDoesNotExistRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final AplicacionRepository aplicacionRepository;

    public ActualizarAplicacionNombreDoesNotExistRuleImpl(final AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    @Override
    public void execute(final ActualizarAplicacionDomain data) {
        final var existingAplicacion = aplicacionRepository.findById(data.getId());
        if (existingAplicacion.isPresent()) {
            final var aplicacion = existingAplicacion.get();
            if (!aplicacion.getNombre().equals(data.getNombre())
                    && aplicacionRepository.existsByNombre(data.getNombre())) {
                throw ConflictException.build(consultarMensajePort.consultarMensaje("MSG-12"));
            }
        }
    }
}
