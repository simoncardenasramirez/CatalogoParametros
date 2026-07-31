package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.crearaplicacionimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.event.CrearAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.publisher.CrearAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import org.springframework.stereotype.Service;

@Service
public class CrearAplicacionImpl implements CrearAplicacion {

    private final AplicacionRepository aplicacionRepository;
    private final CrearAplicacionPublisher crearAplicacionPublisher;
    private final CrearAplicacionRuleValidator crearAplicacionRuleValidator;

    public CrearAplicacionImpl(final AplicacionRepository aplicacionRepository,
            final CrearAplicacionPublisher crearAplicacionPublisher,
            final CrearAplicacionRuleValidator crearAplicacionRuleValidator) {
        this.aplicacionRepository = aplicacionRepository;
        this.crearAplicacionPublisher = crearAplicacionPublisher;
        this.crearAplicacionRuleValidator = crearAplicacionRuleValidator;
    }

    @Override
    public void execute(final CrearAplicacionDomain data) {
        crearAplicacionRuleValidator.validate(data);
        data.generateId();

        var entity = AplicacionEntity.create(data.getId(), data.getNombre(), data.getIdOrganizacion(),
                data.isActiva(), data.getFechaInicio(), data.getFechaFinal());
        var savedEntity = aplicacionRepository.save(entity);
        crearAplicacionPublisher.sendEvent(CrearAplicacionEvent.created(savedEntity));
    }
}
