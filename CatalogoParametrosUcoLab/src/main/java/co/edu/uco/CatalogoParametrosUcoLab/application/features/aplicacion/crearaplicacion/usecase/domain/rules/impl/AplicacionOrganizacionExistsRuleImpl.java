package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.exception.AplicacionException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionOrganizacionExistsRule;

@Service
public final class AplicacionOrganizacionExistsRuleImpl implements AplicacionOrganizacionExistsRule {

    private final OrganizacionRepository organizacionRepository;

    public AplicacionOrganizacionExistsRuleImpl(final OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    @Override
    public void execute(final CrearAplicacionDomain data) {
        final UUID idOrganizacion = data.getIdOrganizacion();
        if (idOrganizacion == null || organizacionRepository.findById(idOrganizacion).isEmpty()) {
            throw new AplicacionException("La organizacion con id " + idOrganizacion + " no existe.");
        }
    }
}
