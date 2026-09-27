package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.crearestadoambienteimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.CrearEstadoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.CrearEstadoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.secondaryports.event.CrearEstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.secondaryports.publisher.CrearEstadoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.CrearEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearEstadoAmbienteImpl implements CrearEstadoAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrearEstadoAmbienteImpl.class);
    private static final String OPERATION_NAME = "crear-estado-ambiente";
    private final EstadoAmbienteRepository repository;
    private final CrearEstadoAmbientePublisher publisher;
    private final TelemetryService telemetryService;
    private final CrearEstadoAmbienteRuleValidator ruleValidator;

    public CrearEstadoAmbienteImpl(final EstadoAmbienteRepository repository,
            final CrearEstadoAmbientePublisher publisher, final TelemetryService telemetryService,
            final CrearEstadoAmbienteRuleValidator ruleValidator) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
        this.ruleValidator = ruleValidator;
    }

    @Override
    public EstadoAmbienteEntity execute(final CrearEstadoAmbienteDomain data) {
        return telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[CREAR-ESTADO-AMBIENTE] Iniciando creacion de estado de ambiente: {}", data.getNombre());
            ruleValidator.validate(data);
            final var entity = repository.save(EstadoAmbienteEntity.create(UUIDHelper.generate(), data.getNombre()));
            publisher.sendEvent(CrearEstadoAmbienteEvent.created(entity));
            LOGGER.info("[CREAR-ESTADO-AMBIENTE] Estado creado exitosamente con id: {}", entity.getId());
            return entity;
        });
    }
}
