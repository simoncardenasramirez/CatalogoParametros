package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroFuncionalidadExistsRule;

@Service
public final class ParametroFuncionalidadExistsRuleImpl implements ParametroFuncionalidadExistsRule {

    private final FuncionalidadRepository funcionalidadRepository;

    public ParametroFuncionalidadExistsRuleImpl(final FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    @Override
    public void execute(final ParametroDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getIdFuncionalidad())) {
            throw new ParametroException("La funcionalidad asociada al parametro es obligatoria.");
        }

        final FuncionalidadEntity funcionalidad = funcionalidadRepository.findById(data.getIdFuncionalidad())
                .orElse(null);

        if (funcionalidad == null || UUIDHelper.getDefault().equals(funcionalidad.getId())) {
            throw new ParametroException(
                    "La funcionalidad con el id " + data.getIdFuncionalidad() + " no existe en el sistema.");
        }
    }
}
