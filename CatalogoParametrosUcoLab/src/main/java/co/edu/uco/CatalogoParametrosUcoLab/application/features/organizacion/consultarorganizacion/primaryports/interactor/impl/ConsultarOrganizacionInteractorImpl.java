package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.consultarorganizacion.primaryports.interactor.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.consultarorganizacion.primaryports.interactor.ConsultarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public class ConsultarOrganizacionInteractorImpl implements ConsultarOrganizacionInteractor {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final OrganizacionRepository organizacionRepository;

    public ConsultarOrganizacionInteractorImpl(final OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    @Override
    public List<OrganizacionEntity> execute() {
        return organizacionRepository.findAll();
    }

    @Override
    public List<OrganizacionEntity> execute(final UUID id) {
        return organizacionRepository.findById(id)
                .map(List::of)
                .orElseThrow(() -> NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-98")));
    }
}