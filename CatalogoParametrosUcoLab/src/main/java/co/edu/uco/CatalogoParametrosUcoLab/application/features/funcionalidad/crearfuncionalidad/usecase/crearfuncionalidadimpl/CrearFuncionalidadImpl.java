package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.crearfuncionalidadimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.event.CrearFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.publisher.CrearFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.CrearFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.CrearFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;

@Service
public class CrearFuncionalidadImpl implements CrearFuncionalidad {

    private static final Logger logger = LoggerFactory.getLogger(CrearFuncionalidadImpl.class);
    private static final String OPERATION_NAME = "crear-funcionalidad";

    private final FuncionalidadRepository funcionalidadRepository;
    private final CrearFuncionalidadPublisher crearFuncionalidadPublisher;
    private final CrearFuncionalidadRuleValidator crearFuncionalidadRuleValidator;
    private final TelemetryService telemetryService;

    public CrearFuncionalidadImpl(final FuncionalidadRepository funcionalidadRepository,
            final CrearFuncionalidadPublisher crearFuncionalidadPublisher,
            final CrearFuncionalidadRuleValidator crearFuncionalidadRuleValidator,
            final TelemetryService telemetryService) {
        this.funcionalidadRepository = funcionalidadRepository;
        this.crearFuncionalidadPublisher = crearFuncionalidadPublisher;
        this.crearFuncionalidadRuleValidator = crearFuncionalidadRuleValidator;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final CrearFuncionalidadDomain data) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[CREAR-FUNCIONALIDAD] Iniciando creacion de funcionalidad: {}", data.getNombre());
            crearFuncionalidadRuleValidator.validate(data);
            data.generateId();

            var entity = FuncionalidadEntity.create(data.getId(), data.getNombre(), data.getIdModulo(),
                    data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
            var savedEntity = funcionalidadRepository.save(entity);
            crearFuncionalidadPublisher.sendEvent(CrearFuncionalidadEvent.created(savedEntity));
            logger.info("[CREAR-FUNCIONALIDAD] Funcionalidad creada exitosamente con id: {}", savedEntity.getId());
        });
    }
}