package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class OrganizacionNameDoesNotExistRuleImpl implements OrganizacionNameDoesNotExistRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final OrganizacionRepository organizacionRepository;

    public OrganizacionNameDoesNotExistRuleImpl(final OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    @Override
    public void execute(final CrearOrganizacionDomain data) {
        if (organizacionRepository.existsByNombre(data.getNombre())) {
            throw ConflictException.build(consultarMensajePort.consultarMensaje("MSG-101") + data.getNombre());
        }
    }
}
