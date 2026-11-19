package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.message.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.message.ConsultarMensajeDetalleInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajeDetallePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeDetalle;

@Service
public final class ConsultarMensajeDetalleInteractorImpl implements ConsultarMensajeDetalleInteractor {

    private final ConsultarMensajeDetallePort consultarMensajeDetallePort;

    public ConsultarMensajeDetalleInteractorImpl(final ConsultarMensajeDetallePort consultarMensajeDetallePort) {
        this.consultarMensajeDetallePort = consultarMensajeDetallePort;
    }

    @Override
    public MensajeDetalle execute(final String codigo) {
        return consultarMensajeDetallePort.consultarDetalle(codigo);
    }
}
