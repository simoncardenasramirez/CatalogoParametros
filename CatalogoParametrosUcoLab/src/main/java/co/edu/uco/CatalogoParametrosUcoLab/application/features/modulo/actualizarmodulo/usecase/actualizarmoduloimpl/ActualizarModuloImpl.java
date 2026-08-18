package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.actualizarmoduloimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.ActualizarModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.ActualizarModuloRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.event.ActualizarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class ActualizarModuloImpl implements ActualizarModulo {

    private static final Logger logger = LoggerFactory.getLogger(ActualizarModuloImpl.class);
    private static final String OPERATION_NAME = "actualizar-modulo";
    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final ModuloRepository moduloRepository;
    private final ActualizarModuloPublisher actualizarModuloPublisher;
    private final ActualizarModuloRuleValidator actualizarModuloRuleValidator;
    private final TelemetryService telemetryService;

    public ActualizarModuloImpl(final ModuloRepository moduloRepository,
            final ActualizarModuloPublisher actualizarModuloPublisher,
            final ActualizarModuloRuleValidator actualizarModuloRuleValidator,
            final TelemetryService telemetryService) {
        this.moduloRepository = moduloRepository;
        this.actualizarModuloPublisher = actualizarModuloPublisher;
        this.actualizarModuloRuleValidator = actualizarModuloRuleValidator;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final ActualizarModuloDomain data) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[ACTUALIZAR-MODULO] Iniciando actualizacion de modulo con id: {}", data.getId());

            if (data == null || UUIDHelper.getDefault().equals(data.getId())) {
                throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-70"));
            }

            if (moduloRepository.findById(data.getId()).isEmpty()) {
                throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-69"));
            }

            actualizarModuloRuleValidator.validate(data);

            var entity = ModuloEntity.create(data.getId(), data.getNombre(), data.getIdAplicacion(),
                    data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
            var updatedEntity = moduloRepository.update(entity);
            actualizarModuloPublisher.sendEvent(ActualizarModuloEvent.updated(updatedEntity));
            logger.info("[ACTUALIZAR-MODULO] Modulo actualizado exitosamente con id: {}", updatedEntity.getId());
        });
    }
}
