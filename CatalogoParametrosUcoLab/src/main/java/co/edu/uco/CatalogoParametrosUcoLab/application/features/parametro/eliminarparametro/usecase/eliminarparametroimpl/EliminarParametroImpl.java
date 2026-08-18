package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.usecase.eliminarparametroimpl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.EliminarParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.event.EliminarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.publisher.EliminarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class EliminarParametroImpl implements EliminarParametro {

    private static final Logger logger = LoggerFactory.getLogger(EliminarParametroImpl.class);
    private static final String OPERATION_NAME = "eliminar-parametro";
    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final ParametroRepository parametroRepository;
    private final EliminarParametroPublisher eliminarParametroPublisher;
    private final TelemetryService telemetryService;

    public EliminarParametroImpl(final ParametroRepository parametroRepository,
            final EliminarParametroPublisher eliminarParametroPublisher,
            final TelemetryService telemetryService) {
        this.parametroRepository = parametroRepository;
        this.eliminarParametroPublisher = eliminarParametroPublisher;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final UUID data) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[ELIMINAR-PARAMETRO] Iniciando eliminacion de parametro con id: {}", data);
                
            if (data == null || UUIDHelper.getDefault().equals(data)) {
                throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-139"));
            }

            var parametro = parametroRepository.findById(data)
                    .orElseThrow(() -> NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-138")));

            parametroRepository.deleteById(data);
            eliminarParametroPublisher.sendEvent(EliminarParametroEvent.deleted(parametro));
            logger.info("[ELIMINAR-PARAMETRO] Parametro eliminado exitosamente con id: {}", data);
        });
    }
}
