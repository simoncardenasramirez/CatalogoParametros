package co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.ModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.exception.ModuloException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.rules.ModuloNombreDoesNotExistRule;

@Service
public final class ModuloNombreDoesNotExistRuleImpl implements ModuloNombreDoesNotExistRule {

    private final ModuloRepository moduloRepository;

    public ModuloNombreDoesNotExistRuleImpl(final ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Override
    public void execute(final ModuloDomain data) {
        if (moduloRepository.existsByNombre(data.getNombre())) {
            throw new ModuloException("Ya existe un modulo con el nombre " + data.getNombre() + ".");
        }
    }
}
