package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.eliminarfuncionalidadimpl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.EliminarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.event.EliminarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.publisher.EliminarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.EliminarFuncionalidadIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.EliminarFuncionalidadIsNotUsedByParametroRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class EliminarFuncionalidadImpl implements EliminarFuncionalidad {

    private static final Logger logger = LoggerFactory.getLogger(EliminarFuncionalidadImpl.class);
    private static final String OPERATION_NAME = "eliminar-funcionalidad";
    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final FuncionalidadRepository funcionalidadRepository;
    private final ParametroRepository parametroRepository;
    private final EliminarFuncionalidadPublisher eliminarFuncionalidadPublisher;
    private final EliminarFuncionalidadIdExistsRule eliminarFuncionalidadIdExistsRule;
    private final EliminarFuncionalidadIsNotUsedByParametroRule eliminarFuncionalidadIsNotUsedByParametroRule;
    private final TelemetryService telemetryService;

    public EliminarFuncionalidadImpl(final FuncionalidadRepository funcionalidadRepository,
            final ParametroRepository parametroRepository,
            final EliminarFuncionalidadPublisher eliminarFuncionalidadPublisher,
            final EliminarFuncionalidadIdExistsRule eliminarFuncionalidadIdExistsRule,
            final EliminarFuncionalidadIsNotUsedByParametroRule eliminarFuncionalidadIsNotUsedByParametroRule,
            final TelemetryService telemetryService) {
        this.funcionalidadRepository = funcionalidadRepository;
        this.parametroRepository = parametroRepository;
        this.eliminarFuncionalidadPublisher = eliminarFuncionalidadPublisher;
        this.eliminarFuncionalidadIdExistsRule = eliminarFuncionalidadIdExistsRule;
        this.eliminarFuncionalidadIsNotUsedByParametroRule = eliminarFuncionalidadIsNotUsedByParametroRule;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final UUID data) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[ELIMINAR-FUNCIONALIDAD] Iniciando eliminacion de funcionalidad con id: {}", data);
            if (data == null || UUIDHelper.getDefault().equals(data)) {
                throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-61"));
            }

            eliminarFuncionalidadIdExistsRule.execute(data);
            eliminarFuncionalidadIsNotUsedByParametroRule.execute(data);
            
            
            var funcionalidad = funcionalidadRepository.findById(data)
                .orElseThrow(() -> NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-60")));

            funcionalidadRepository.deleteById(data);
            eliminarFuncionalidadPublisher.sendEvent(EliminarFuncionalidadEvent.deleted(funcionalidad));
            logger.info("[ELIMINAR-FUNCIONALIDAD] Funcionalidad eliminada exitosamente con id: {}", data);
        });
    }
}