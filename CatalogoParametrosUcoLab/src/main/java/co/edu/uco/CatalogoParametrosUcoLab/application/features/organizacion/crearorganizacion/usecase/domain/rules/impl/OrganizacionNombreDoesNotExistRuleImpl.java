package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.exception.OrganizacionException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNombreDoesNotExistRule;

@Service
public final class OrganizacionNombreDoesNotExistRuleImpl implements OrganizacionNombreDoesNotExistRule {

    private final OrganizacionRepository organizacionRepository;

    public OrganizacionNombreDoesNotExistRuleImpl(final OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    @Override
    public void execute(final CrearOrganizacionDomain data) {
        if (organizacionRepository.existsByNombre(data.getNombre())) {
            throw new OrganizacionException("Ya existe una organizacion con el nombre " + data.getNombre() + ".");
        }
    }
}
