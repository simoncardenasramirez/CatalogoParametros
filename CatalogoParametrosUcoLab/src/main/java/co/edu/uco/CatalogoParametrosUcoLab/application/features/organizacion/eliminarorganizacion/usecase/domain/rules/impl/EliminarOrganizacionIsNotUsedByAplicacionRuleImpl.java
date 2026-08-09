package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.EliminarOrganizacionIsNotUsedByAplicacionRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.validator.RuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public class EliminarOrganizacionIsNotUsedByAplicacionRuleImpl implements EliminarOrganizacionIsNotUsedByAplicacionRule {

    private final AplicacionRepository aplicacionRepository;

    public EliminarOrganizacionIsNotUsedByAplicacionRuleImpl(final AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    @Override
    public void execute(final UUID id) {
        if (aplicacionRepository.existsByIdOrganizacion(id)) {
            throw ConflictException.build(
                    "No se puede eliminar la organizacion porque esta siendo usada por una o mas aplicaciones.");
        }
    }
}
