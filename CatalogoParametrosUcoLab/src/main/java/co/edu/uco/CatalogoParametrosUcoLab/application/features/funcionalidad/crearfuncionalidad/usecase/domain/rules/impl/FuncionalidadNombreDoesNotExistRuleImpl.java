package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNombreDoesNotExistRule;

@Service
public final class FuncionalidadNombreDoesNotExistRuleImpl implements FuncionalidadNombreDoesNotExistRule {

    private final FuncionalidadRepository funcionalidadRepository;

    public FuncionalidadNombreDoesNotExistRuleImpl(final FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    @Override
    public void execute(final CrearFuncionalidadDomain data) {
        if (funcionalidadRepository.existsByNombre(data.getNombre())) {
            throw new FuncionalidadException("Ya existe una funcionalidad con el nombre " + data.getNombre() + ".");
        }
    }
}
