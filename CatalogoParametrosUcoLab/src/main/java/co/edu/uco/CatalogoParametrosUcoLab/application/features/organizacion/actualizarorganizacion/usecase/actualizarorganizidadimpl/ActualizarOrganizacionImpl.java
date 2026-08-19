package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.actualizarorganizidadimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.ActualizarOrganizacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.event.ActualizarOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.publisher.ActualizarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.ActualizarOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public class ActualizarOrganizacionImpl implements ActualizarOrganizacion {

    private static final Logger logger = LoggerFactory.getLogger(ActualizarOrganizacionImpl.class);
    private static final String OPERATION_NAME = "actualizar-organizacion";
    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final OrganizacionRepository organizacionRepository;
    private final ActualizarOrganizacionPublisher actualizarOrganizacionPublisher;
    private final TelemetryService telemetryService;
    private final ActualizarOrganizacionRuleValidator actualizarOrganizacionRuleValidator;

    public ActualizarOrganizacionImpl(final OrganizacionRepository organizacionRepository,
            final ActualizarOrganizacionPublisher actualizarOrganizacionPublisher,
            final TelemetryService telemetryService,
            final ActualizarOrganizacionRuleValidator actualizarOrganizacionRuleValidator) {
        this.organizacionRepository = organizacionRepository;
        this.actualizarOrganizacionPublisher = actualizarOrganizacionPublisher;
        this.telemetryService = telemetryService;
        this.actualizarOrganizacionRuleValidator = actualizarOrganizacionRuleValidator;
    }

    @Override
    @Transactional
    public void execute(final ActualizarOrganizacionDomain domain) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[ACTUALIZAR-ORGANIZACION] Iniciando actualizacion de organizacion con id: {}", domain.getId());
            if (organizacionRepository.findById(domain.getId()).isEmpty()) {
                throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-93"));
            }
            actualizarOrganizacionRuleValidator.validate(domain);
            var entity = OrganizacionEntity.create(domain.getId(), domain.getNombre());
            var updatedEntity = organizacionRepository.update(entity);
            actualizarOrganizacionPublisher.sendEvent(ActualizarOrganizacionEvent.updated(updatedEntity));
            logger.info("[ACTUALIZAR-ORGANIZACION] Organizacion actualizada exitosamente con id: {}", updatedEntity.getId());
        });
    }
}
