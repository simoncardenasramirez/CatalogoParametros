package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNombreIsNotEmptyRule;

@Service
public final class OrganizacionNombreIsNotEmptyRuleImpl implements OrganizacionNombreIsNotEmptyRule {

    @Override
    public void execute(final CrearOrganizacionDomain data) {
        if (data == null || TextHelper.isBlank(data.getNombre())) {
            throw ValidationException.build("El nombre de la organizacion no puede estar vacio.");
        }
    }
}
