package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarFuncionalidadIdExistsRuleImpl implements ActualizarFuncionalidadIdExistsRule {

    private final FuncionalidadRepository funcionalidadRepository;

    public ActualizarFuncionalidadIdExistsRuleImpl(final FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    @Override
    public void execute(final ActualizarFuncionalidadDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getId())) {
            throw new co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException(
                    "El id de la funcionalidad es obligatorio para actualizar.");
        }

        if (funcionalidadRepository.findById(data.getId()).isEmpty()) {
            throw new co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException(
                    "No existe una funcionalidad con el id especificado.");
        }
    }
}