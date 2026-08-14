package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.crearorganizacionimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.event.CrearOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.publisher.CrearOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrearOrganizacionImpl implements CrearOrganizacion {

    private static final Logger logger = LoggerFactory.getLogger(CrearOrganizacionImpl.class);
    private static final String OPERATION_NAME = "crear-organizacion";

    private final OrganizacionRepository organizacionRepository;
    private final CrearOrganizacionPublisher crearOrganizacionPublisher;
    private final CrearOrganizacionRuleValidator crearOrganizacionRuleValidator;
    private final TelemetryService telemetryService;

    public CrearOrganizacionImpl(final OrganizacionRepository organizacionRepository,
            final CrearOrganizacionPublisher crearOrganizacionPublisher,
            final CrearOrganizacionRuleValidator crearOrganizacionRuleValidator,
            final TelemetryService telemetryService) {
        this.organizacionRepository = organizacionRepository;
        this.crearOrganizacionPublisher = crearOrganizacionPublisher;
        this.crearOrganizacionRuleValidator = crearOrganizacionRuleValidator;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final CrearOrganizacionDomain data) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[CREAR-ORGANIZACION] Iniciando creacion de organizacion: {}", data.getNombre());
            crearOrganizacionRuleValidator.validate(data);
            data.generateId();

            var entity = OrganizacionEntity.create(data.getId(), data.getNombre());
            var savedEntity = organizacionRepository.save(entity);
            crearOrganizacionPublisher.sendEvent(CrearOrganizacionEvent.created(savedEntity));
            logger.info("[CREAR-ORGANIZACION] Organizacion creada exitosamente con id: {}", savedEntity.getId());
        });
    }
}
