package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNombreIsNotNullRule;

@Service
public final class OrganizacionNombreIsNotNullRuleImpl implements OrganizacionNombreIsNotNullRule {

    @Override
    public void execute(final CrearOrganizacionDomain data) {
        if (data == null || data.getNombre() == null) {
            throw ValidationException.build("El nombre de la organizacion es obligatorio.");
        }
    }
}
