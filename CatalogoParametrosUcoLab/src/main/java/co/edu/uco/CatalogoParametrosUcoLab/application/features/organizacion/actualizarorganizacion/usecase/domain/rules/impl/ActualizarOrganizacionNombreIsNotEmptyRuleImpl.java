package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.validator.RuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public class ActualizarOrganizacionNombreIsNotEmptyRuleImpl implements ActualizarOrganizacionNombreIsNotEmptyRule {

    @Override
    public void execute(final ActualizarOrganizacionDomain data) {
        if (data.getNombre().isBlank()) {
            throw ValidationException.build("El nombre de la organizacion no puede estar vacio.");
        }
    }
}
