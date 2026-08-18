package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.crearaplicacionimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.event.CrearAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.publisher.CrearAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrearAplicacionImpl implements CrearAplicacion {

    private static final Logger logger = LoggerFactory.getLogger(CrearAplicacionImpl.class);
    private static final String OPERATION_NAME = "crear-aplicacion";

    private final AplicacionRepository aplicacionRepository;
    private final CrearAplicacionPublisher crearAplicacionPublisher;
    private final CrearAplicacionRuleValidator crearAplicacionRuleValidator;
    private final TelemetryService telemetryService;

    public CrearAplicacionImpl(final AplicacionRepository aplicacionRepository,
            final CrearAplicacionPublisher crearAplicacionPublisher,
            final CrearAplicacionRuleValidator crearAplicacionRuleValidator,
            final TelemetryService telemetryService) {
        this.aplicacionRepository = aplicacionRepository;
        this.crearAplicacionPublisher = crearAplicacionPublisher;
        this.crearAplicacionRuleValidator = crearAplicacionRuleValidator;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final CrearAplicacionDomain data) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[CREAR-APLICACION] Iniciando creacion de aplicacion: {}", data.getNombre());
            crearAplicacionRuleValidator.validate(data);
            data.generateId();

            var entity = AplicacionEntity.create(data.getId(), data.getNombre(), data.getIdOrganizacion(),
                    data.isActiva(), data.getFechaInicio(), data.getFechaFinal());
            var savedEntity = aplicacionRepository.save(entity);
            crearAplicacionPublisher.sendEvent(CrearAplicacionEvent.created(savedEntity));
            logger.info("[CREAR-APLICACION] Aplicacion creada exitosamente con id: {}", savedEntity.getId());
        });
    }
}
