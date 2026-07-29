package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.EliminarFuncionalidadIsNotUsedByParametroRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;

@Service
public final class EliminarFuncionalidadIsNotUsedByParametroRuleImpl implements EliminarFuncionalidadIsNotUsedByParametroRule {

    private final ParametroRepository parametroRepository;

    public EliminarFuncionalidadIsNotUsedByParametroRuleImpl(final ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public void execute(final UUID data) {
        final var parametros = parametroRepository.findByIdFuncionalidad(data);
        if (parametros != null && !parametros.isEmpty()) {
            throw new FuncionalidadException(
                    "No se puede eliminar la funcionalidad con el id " + data
                            + " porque esta siendo utilizada por uno o mas parametros.");
        }
    }
}