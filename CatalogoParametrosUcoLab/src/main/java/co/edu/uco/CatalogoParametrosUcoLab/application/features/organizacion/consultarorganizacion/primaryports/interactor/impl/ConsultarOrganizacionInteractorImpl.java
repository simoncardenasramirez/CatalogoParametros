package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.consultarorganizacion.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.consultarorganizacion.primaryports.interactor.ConsultarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;

@Service
public class ConsultarOrganizacionInteractorImpl implements ConsultarOrganizacionInteractor {

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
                .orElse(List.of());
    }
}