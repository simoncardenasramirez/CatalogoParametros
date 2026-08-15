package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.actualizarfuncionalidadimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.event.ActualizarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class ActualizarFuncionalidadImpl implements ActualizarFuncionalidad {

    private static final Logger logger = LoggerFactory.getLogger(ActualizarFuncionalidadImpl.class);
    private static final String OPERATION_NAME = "actualizar-funcionalidad";
    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final FuncionalidadRepository funcionalidadRepository;
    private final ActualizarFuncionalidadPublisher actualizarFuncionalidadPublisher;
    private final ActualizarFuncionalidadRuleValidator actualizarFuncionalidadRuleValidator;
    private final TelemetryService telemetryService;

    public ActualizarFuncionalidadImpl(final FuncionalidadRepository funcionalidadRepository,
            final ActualizarFuncionalidadPublisher actualizarFuncionalidadPublisher,
            final ActualizarFuncionalidadRuleValidator actualizarFuncionalidadRuleValidator,
            final TelemetryService telemetryService) {
        this.funcionalidadRepository = funcionalidadRepository;
        this.actualizarFuncionalidadPublisher = actualizarFuncionalidadPublisher;
        this.actualizarFuncionalidadRuleValidator = actualizarFuncionalidadRuleValidator;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final ActualizarFuncionalidadDomain data) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[ACTUALIZAR-FUNCIONALIDAD] Iniciando actualizacion de funcionalidad con id: {}", data.getId());
            
            if (data == null || UUIDHelper.getDefault().equals(data.getId())) {
                throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-37"));
            }

            if (funcionalidadRepository.findById(data.getId()).isEmpty()) {
                throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-36"));
            }

            actualizarFuncionalidadRuleValidator.validate(data);

            var entity = FuncionalidadEntity.create(data.getId(), data.getNombre(), data.getIdModulo(),
                    data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
            var updatedEntity = funcionalidadRepository.update(entity);
            actualizarFuncionalidadPublisher.sendEvent(ActualizarFuncionalidadEvent.updated(updatedEntity));
            logger.info("[ACTUALIZAR-FUNCIONALIDAD] Funcionalidad actualizada exitosamente con id: {}", updatedEntity.getId());
        });
    }
}