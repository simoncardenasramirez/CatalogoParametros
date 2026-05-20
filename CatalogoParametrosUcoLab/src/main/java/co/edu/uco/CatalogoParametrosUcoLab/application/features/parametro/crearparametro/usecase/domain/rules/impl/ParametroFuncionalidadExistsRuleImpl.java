package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroFuncionalidadExistsRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;


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
