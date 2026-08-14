package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.validator.RuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public class ActualizarOrganizacionIdExistsRuleImpl implements ActualizarOrganizacionIdExistsRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final OrganizacionRepository organizacionRepository;

    public ActualizarOrganizacionIdExistsRuleImpl(final OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    @Override
    public void execute(final ActualizarOrganizacionDomain data) {
        final var id = data.getId();
        if (organizacionRepository.findById(id).isEmpty()) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-94"));
        }
    }
}
