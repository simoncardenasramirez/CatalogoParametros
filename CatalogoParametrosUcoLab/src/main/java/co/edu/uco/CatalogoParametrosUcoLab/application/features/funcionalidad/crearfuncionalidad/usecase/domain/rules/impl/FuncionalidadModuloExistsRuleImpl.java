package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.FuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadModuloExistsRule;
import org.springframework.stereotype.Service;

@Service
public final class FuncionalidadModuloExistsRuleImpl
        implements FuncionalidadModuloExistsRule {

    private final ModuloRepository moduloRepository;

    public FuncionalidadModuloExistsRuleImpl(
            final ModuloRepository moduloRepository) {

        this.moduloRepository = moduloRepository;
    }

    @Override
    public void execute(final FuncionalidadDomain data) {

        if (data == null
                || UUIDHelper.getDefault().equals(data.getIdModulo())) {

            throw new FuncionalidadException(
                    "El modulo asociado a la funcionalidad es obligatorio.");
        }

        final ModuloEntity modulo = moduloRepository
                .findById(data.getIdModulo())
                .orElse(null);

        if (modulo == null
                || UUIDHelper.getDefault().equals(modulo.getId())) {

            throw new FuncionalidadException(
                    "El modulo con el id "
                            + data.getIdModulo()
                            + " no existe en el sistema.");
        }
    }
}