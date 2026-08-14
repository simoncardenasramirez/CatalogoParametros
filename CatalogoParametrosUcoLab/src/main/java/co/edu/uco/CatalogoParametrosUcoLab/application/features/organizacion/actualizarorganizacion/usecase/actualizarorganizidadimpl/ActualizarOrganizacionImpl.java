package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.actualizarorganizidadimpl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.event.ActualizarOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.publisher.ActualizarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.ActualizarOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public class ActualizarOrganizacionImpl implements ActualizarOrganizacion {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final OrganizacionRepository organizacionRepository;
    private final ActualizarOrganizacionPublisher actualizarOrganizacionPublisher;

    public ActualizarOrganizacionImpl(final OrganizacionRepository organizacionRepository,
            final ActualizarOrganizacionPublisher actualizarOrganizacionPublisher) {
        this.organizacionRepository = organizacionRepository;
        this.actualizarOrganizacionPublisher = actualizarOrganizacionPublisher;
    }

    @Override
    @Transactional
    public void execute(final ActualizarOrganizacionDomain domain) {
        if (organizacionRepository.findById(domain.getId()).isEmpty()) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-93"));
        }
        var entity = OrganizacionEntity.create(domain.getId(), domain.getNombre());
        var updatedEntity = organizacionRepository.update(entity);
        actualizarOrganizacionPublisher.sendEvent(ActualizarOrganizacionEvent.updated(updatedEntity));
    }
}
