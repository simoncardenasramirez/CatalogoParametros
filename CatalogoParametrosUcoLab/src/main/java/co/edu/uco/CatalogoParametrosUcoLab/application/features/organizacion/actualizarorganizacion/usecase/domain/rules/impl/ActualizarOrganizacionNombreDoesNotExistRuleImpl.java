package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.validator.RuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public class ActualizarOrganizacionNombreDoesNotExistRuleImpl implements ActualizarOrganizacionNombreDoesNotExistRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final OrganizacionRepository organizacionRepository;

    public ActualizarOrganizacionNombreDoesNotExistRuleImpl(final OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    @Override
    public void execute(final ActualizarOrganizacionDomain data) {
        final var nombre = data.getNombre();
        final var existing = organizacionRepository.findAll().stream()
                .filter(o -> o.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
        if (existing.isPresent() && !existing.get().getId().equals(data.getId())) {
            throw ConflictException.build(consultarMensajePort.consultarMensaje("MSG-95"));
        }
    }
}
