package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionOrganizacionExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarAplicacionOrganizacionExistsRuleImpl implements ActualizarAplicacionOrganizacionExistsRule {

    private final OrganizacionRepository organizacionRepository;

    public ActualizarAplicacionOrganizacionExistsRuleImpl(final OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    @Override
    public void execute(final ActualizarAplicacionDomain data) {
        final UUID idOrganizacion = data.getIdOrganizacion();
        if (idOrganizacion == null || organizacionRepository.findById(idOrganizacion).isEmpty()) {
            throw NotFoundException.build("La organizacion con id " + idOrganizacion + " no existe.");
        }
    }
}
