package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.exception.ModuloException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreDoesNotExistRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;

@Service
public final class ModuloNombreDoesNotExistRuleImpl implements ModuloNombreDoesNotExistRule {

    private final ModuloRepository moduloRepository;

    public ModuloNombreDoesNotExistRuleImpl(final ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Override
    public void execute(final CrearModuloDomain data) {
        if (moduloRepository.existsByNombre(data.getNombre())) {
            throw new ModuloException("Ya existe un modulo con el nombre " + data.getNombre() + ".");
        }
    }
}
