package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;

@Service
public final class ActualizarFuncionalidadNombreDoesNotExistRuleImpl implements ActualizarFuncionalidadNombreDoesNotExistRule {

    private final FuncionalidadRepository funcionalidadRepository;

    public ActualizarFuncionalidadNombreDoesNotExistRuleImpl(final FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    @Override
    public void execute(final ActualizarFuncionalidadDomain data) {
        final var existingFuncionalidad = funcionalidadRepository.findById(data.getId());
        if (existingFuncionalidad.isPresent()) {
            final var funcionalidad = existingFuncionalidad.get();
            if (!funcionalidad.getNombre().equals(data.getNombre())
                    && funcionalidadRepository.existsByNombre(data.getNombre())) {
                throw new FuncionalidadException(
                        "Ya existe una funcionalidad con el nombre " + data.getNombre() + ".");
            }
        }
    }
}