package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.crearparametroimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.event.CrearParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametroRuleValidator;

@Service
public class CrearParametroImpl implements CrearParametro {

    private static final Logger logger = LoggerFactory.getLogger(CrearParametroImpl.class);
    private static final String OPERATION_NAME = "crear-parametro";

    private final ParametroRepository parametroRepository;
    private final CrearParametroPublisher crearParametroPublisher;
    private final CrearParametroRuleValidator crearParametroRuleValidator;
    private final TelemetryService telemetryService;

    public CrearParametroImpl(final ParametroRepository parametroRepository,
            final CrearParametroPublisher crearParametroPublisher,
            final CrearParametroRuleValidator crearParametroRuleValidator,
            final TelemetryService telemetryService) {
        this.parametroRepository = parametroRepository;
        this.crearParametroPublisher = crearParametroPublisher;
        this.crearParametroRuleValidator = crearParametroRuleValidator;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final CrearParametroDomain data) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[CREAR-PARAMETRO] Iniciando creacion de parametro: {}", data.getNombre());
            crearParametroRuleValidator.validate(data);
            data.generateId();

            var entity = ParametroEntity.create(data.getId(), data.getNombre(), data.getIdFuncionalidad(),
                    data.getIdTipoParametro(), data.isActivo());
            var savedEntity = parametroRepository.save(entity);
            crearParametroPublisher.sendEvent(CrearParametroEvent.created(savedEntity));
            logger.info("[CREAR-PARAMETRO] Parametro creado exitosamente con id: {}", savedEntity.getId());
        });
    }
}
