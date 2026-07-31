package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.EliminarOrganizacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.validator.RuleValidator;

@Service
public class EliminarOrganizacionIdExistsRuleImpl implements EliminarOrganizacionIdExistsRule {

    private final OrganizacionRepository organizacionRepository;

    public EliminarOrganizacionIdExistsRuleImpl(final OrganizacionRepository organizacionRepository) {
        this.organizacionRepository = organizacionRepository;
    }

    @Override
    public void execute(final UUID id) {
        if (organizacionRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("La organizacion con id " + id + " no existe.");
        }
    }
}
