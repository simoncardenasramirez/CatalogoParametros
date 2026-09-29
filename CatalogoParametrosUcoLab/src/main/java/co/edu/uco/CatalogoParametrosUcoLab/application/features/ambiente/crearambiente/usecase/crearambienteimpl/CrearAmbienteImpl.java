package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.crearambienteimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.CrearAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.CrearAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.secondaryports.event.CrearAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.secondaryports.publisher.CrearAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.CrearAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearAmbienteImpl implements CrearAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrearAmbienteImpl.class);
    private static final String OPERATION_NAME = "crear-ambiente";
    private final AmbienteRepository repository;
    private final CrearAmbientePublisher publisher;
    private final TelemetryService telemetryService;
    private final CrearAmbienteRuleValidator ruleValidator;

    public CrearAmbienteImpl(final AmbienteRepository repository, final CrearAmbientePublisher publisher,
            final TelemetryService telemetryService, final CrearAmbienteRuleValidator ruleValidator) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
        this.ruleValidator = ruleValidator;
    }

    @Override
    public AmbienteEntity execute(final CrearAmbienteDomain data) {
        return telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[CREAR-AMBIENTE] Iniciando creacion de ambiente: {}", data.getNombre());
            ruleValidator.validate(data);
            final var entity = repository.save(AmbienteEntity.create(UUIDHelper.generate(), data.getNombre()));
            publisher.sendEvent(CrearAmbienteEvent.created(entity));
            LOGGER.info("[CREAR-AMBIENTE] Ambiente creado exitosamente con id: {}", entity.getId());
            return entity;
        });
    }
}
