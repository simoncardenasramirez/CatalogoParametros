package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.crearorganizacionimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.event.CrearOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.publisher.CrearOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import org.springframework.stereotype.Service;

@Service
public class CrearOrganizacionImpl implements CrearOrganizacion {

    private final OrganizacionRepository organizacionRepository;
    private final CrearOrganizacionPublisher crearOrganizacionPublisher;
    private final CrearOrganizacionRuleValidator crearOrganizacionRuleValidator;

    public CrearOrganizacionImpl(final OrganizacionRepository organizacionRepository,
            final CrearOrganizacionPublisher crearOrganizacionPublisher,
            final CrearOrganizacionRuleValidator crearOrganizacionRuleValidator) {
        this.organizacionRepository = organizacionRepository;
        this.crearOrganizacionPublisher = crearOrganizacionPublisher;
        this.crearOrganizacionRuleValidator = crearOrganizacionRuleValidator;
    }

    @Override
    public void execute(final CrearOrganizacionDomain data) {
        crearOrganizacionRuleValidator.validate(data);
        data.generateId();

        var entity = OrganizacionEntity.create(data.getId(), data.getNombre());
        var savedEntity = organizacionRepository.save(entity);
        crearOrganizacionPublisher.sendEvent(CrearOrganizacionEvent.created(savedEntity));
    }
}
