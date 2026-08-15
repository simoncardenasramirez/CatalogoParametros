package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.usecase.eliminarparametroimpl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.EliminarParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.event.EliminarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.publisher.EliminarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class EliminarParametroImpl implements EliminarParametro {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final ParametroRepository parametroRepository;
    private final EliminarParametroPublisher eliminarParametroPublisher;

    public EliminarParametroImpl(final ParametroRepository parametroRepository,
            final EliminarParametroPublisher eliminarParametroPublisher) {
        this.parametroRepository = parametroRepository;
        this.eliminarParametroPublisher = eliminarParametroPublisher;
    }

    @Override
    public void execute(final UUID data) {
        if (data == null || UUIDHelper.getDefault().equals(data)) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-139"));
        }

        var parametro = parametroRepository.findById(data)
                .orElseThrow(() -> NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-138")));

        parametroRepository.deleteById(data);
        eliminarParametroPublisher.sendEvent(EliminarParametroEvent.deleted(parametro));
    }
}
