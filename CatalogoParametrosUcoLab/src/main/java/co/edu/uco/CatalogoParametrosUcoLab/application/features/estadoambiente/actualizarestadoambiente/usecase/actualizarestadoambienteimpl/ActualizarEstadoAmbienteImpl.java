package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.actualizarestadoambienteimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.ActualizarEstadoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.ActualizarEstadoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.secondaryports.event.ActualizarEstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.secondaryports.publisher.ActualizarEstadoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.ActualizarEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;

@Service
public final class ActualizarEstadoAmbienteImpl implements ActualizarEstadoAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActualizarEstadoAmbienteImpl.class);
    private static final String OPERATION_NAME = "actualizar-estado-ambiente";
    private final EstadoAmbienteRepository repository;
    private final ActualizarEstadoAmbientePublisher publisher;
    private final TelemetryService telemetryService;
    private final ActualizarEstadoAmbienteRuleValidator ruleValidator;

    public ActualizarEstadoAmbienteImpl(final EstadoAmbienteRepository repository,
            final ActualizarEstadoAmbientePublisher publisher, final TelemetryService telemetryService,
            final ActualizarEstadoAmbienteRuleValidator ruleValidator) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
        this.ruleValidator = ruleValidator;
    }

    @Override
    public EstadoAmbienteEntity execute(final ActualizarEstadoAmbienteDomain data) {
        return telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[ACTUALIZAR-ESTADO-AMBIENTE] Iniciando actualizacion con id: {}", data.getId());
            ruleValidator.validate(data);
            final var entity = repository.update(EstadoAmbienteEntity.create(data.getId(), data.getNombre()));
            publisher.sendEvent(ActualizarEstadoAmbienteEvent.updated(entity));
            LOGGER.info("[ACTUALIZAR-ESTADO-AMBIENTE] Estado actualizado exitosamente con id: {}", entity.getId());
            return entity;
        });
    }
}
