package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.eliminarorganizacionimpl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.EliminarOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.event.EliminarOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.publisher.EliminarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.EliminarOrganizacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.EliminarOrganizacionIsNotUsedByAplicacionRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;

@Service
public class EliminarOrganizacionImpl implements EliminarOrganizacion {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final OrganizacionRepository organizacionRepository;
    private final EliminarOrganizacionPublisher eliminarOrganizacionPublisher;
    private final EliminarOrganizacionIdExistsRule idExistsRule;
    private final EliminarOrganizacionIsNotUsedByAplicacionRule isNotUsedByAplicacionRule;

    public EliminarOrganizacionImpl(final OrganizacionRepository organizacionRepository,
            final EliminarOrganizacionPublisher eliminarOrganizacionPublisher,
            final EliminarOrganizacionIdExistsRule idExistsRule,
            final EliminarOrganizacionIsNotUsedByAplicacionRule isNotUsedByAplicacionRule) {
        this.organizacionRepository = organizacionRepository;
        this.eliminarOrganizacionPublisher = eliminarOrganizacionPublisher;
        this.idExistsRule = idExistsRule;
        this.isNotUsedByAplicacionRule = isNotUsedByAplicacionRule;
    }

    @Override
    @Transactional
    public void execute(final java.util.UUID id) {
        final List<String> messages = new ArrayList<>();
        try {
            idExistsRule.execute(id);
        } catch (final Exception e) {
            messages.add(e.getMessage());
        }
        try {
            isNotUsedByAplicacionRule.execute(id);
        } catch (final Exception e) {
            messages.add(e.getMessage());
        }
        if (!messages.isEmpty()) {
            throw ValidationException.build(String.join(", ", messages));
        }

        var organizacion = organizacionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-106")));

        organizacionRepository.deleteById(id);
        eliminarOrganizacionPublisher.sendEvent(EliminarOrganizacionEvent.deleted(organizacion));
    }
}
