package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.crearmetadatoambienteimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.CrearMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.CrearMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.event.CrearMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.publisher.CrearMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.CrearMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearMetadatoAmbienteImpl implements CrearMetadatoAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrearMetadatoAmbienteImpl.class);
    private static final String OPERATION_NAME = "crear-metadato-ambiente";
    private final MetadatoAmbienteRepository repository;
    private final CrearMetadatoAmbientePublisher publisher;
    private final TelemetryService telemetryService;
    private final CrearMetadatoAmbienteRuleValidator ruleValidator;

    public CrearMetadatoAmbienteImpl(final MetadatoAmbienteRepository repository,
            final CrearMetadatoAmbientePublisher publisher, final TelemetryService telemetryService,
            final CrearMetadatoAmbienteRuleValidator ruleValidator) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
        this.ruleValidator = ruleValidator;
    }

    @Override
    public MetadatoAmbienteEntity execute(final CrearMetadatoAmbienteDomain data) {
        return telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[CREAR-METADATO-AMBIENTE] Iniciando creacion para parametro: {} y ambiente: {}",
                    data.getIdParametro(), data.getIdAmbiente());
            ruleValidator.validate(data);
            final var entity = repository.save(MetadatoAmbienteEntity.create(UUIDHelper.generate(),
                    data.getIdParametro(), data.getIdAmbiente(), data.getIdEstadoMetadatoAmbiente()));
            publisher.sendEvent(CrearMetadatoAmbienteEvent.created(entity));
            LOGGER.info("[CREAR-METADATO-AMBIENTE] Metadato creado exitosamente con id: {}", entity.getId());
            return entity;
        });
    }

}
