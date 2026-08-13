package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloAplicacionExistsRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ModuloAplicacionExistsRuleImpl implements ModuloAplicacionExistsRule {

    private final AplicacionRepository aplicacionRepository;

    public ModuloAplicacionExistsRuleImpl(final AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    @Override
    public void execute(final CrearModuloDomain data) {
        if (aplicacionRepository.findById(data.getIdAplicacion()).isEmpty()) {
            throw NotFoundException.build("La aplicacion con el id " + data.getIdAplicacion() + " no existe.");
        }
    }
}
