package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.actualizarambienteimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.ActualizarAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.ActualizarAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.secondaryports.event.ActualizarAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.secondaryports.publisher.ActualizarAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.ActualizarAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;

@Service
public final class ActualizarAmbienteImpl implements ActualizarAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActualizarAmbienteImpl.class);
    private static final String OPERATION_NAME = "actualizar-ambiente";
    private final AmbienteRepository repository;
    private final ActualizarAmbientePublisher publisher;
    private final TelemetryService telemetryService;
    private final ActualizarAmbienteRuleValidator ruleValidator;

    public ActualizarAmbienteImpl(final AmbienteRepository repository, final ActualizarAmbientePublisher publisher,
            final TelemetryService telemetryService, final ActualizarAmbienteRuleValidator ruleValidator) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
        this.ruleValidator = ruleValidator;
    }

    @Override
    public AmbienteEntity execute(final ActualizarAmbienteDomain data) {
        return telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[ACTUALIZAR-AMBIENTE] Iniciando actualizacion de ambiente con id: {}", data.getId());
            ruleValidator.validate(data);
            final var entity = repository.update(AmbienteEntity.create(data.getId(), data.getNombre()));
            publisher.sendEvent(ActualizarAmbienteEvent.updated(entity));
            LOGGER.info("[ACTUALIZAR-AMBIENTE] Ambiente actualizado exitosamente con id: {}", entity.getId());
            return entity;
        });
    }
}
