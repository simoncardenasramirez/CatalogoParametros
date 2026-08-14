package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.actualizaraplicacionimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.ActualizarAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.ActualizarAplicacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.event.ActualizarAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class ActualizarAplicacionImpl implements ActualizarAplicacion {

    private static final Logger logger = LoggerFactory.getLogger(ActualizarAplicacionImpl.class);
    private static final String OPERATION_NAME = "actualizar-aplicacion";

    private final AplicacionRepository aplicacionRepository;
    private final ActualizarAplicacionPublisher actualizarAplicacionPublisher;
    private final ActualizarAplicacionRuleValidator actualizarAplicacionRuleValidator;
    private final TelemetryService telemetryService;

    public ActualizarAplicacionImpl(final AplicacionRepository aplicacionRepository,
            final ActualizarAplicacionPublisher actualizarAplicacionPublisher,
            final ActualizarAplicacionRuleValidator actualizarAplicacionRuleValidator,
            final TelemetryService telemetryService) {
        this.aplicacionRepository = aplicacionRepository;
        this.actualizarAplicacionPublisher = actualizarAplicacionPublisher;
        this.actualizarAplicacionRuleValidator = actualizarAplicacionRuleValidator;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final ActualizarAplicacionDomain data) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[ACTUALIZAR-APLICACION] Iniciando actualizacion de aplicacion con id: {}", data.getId());

            if (data == null || UUIDHelper.getDefault().equals(data.getId())) {
                throw ValidationException.build(
                        "El id de la aplicacion es obligatorio para actualizar.");
            }

            if (aplicacionRepository.findById(data.getId()).isEmpty()) {
                throw NotFoundException.build(
                        "No existe una aplicacion con el id especificado.");
            }

            actualizarAplicacionRuleValidator.validate(data);

            var entity = AplicacionEntity.create(data.getId(), data.getNombre(), data.getIdOrganizacion(),
                    data.isActiva(), data.getFechaInicio(), data.getFechaFinal());
            var updatedEntity = aplicacionRepository.update(entity);
            actualizarAplicacionPublisher.sendEvent(ActualizarAplicacionEvent.updated(updatedEntity));
            logger.info("[ACTUALIZAR-APLICACION] Aplicacion actualizada exitosamente con id: {}", updatedEntity.getId());
        });
    }
}
