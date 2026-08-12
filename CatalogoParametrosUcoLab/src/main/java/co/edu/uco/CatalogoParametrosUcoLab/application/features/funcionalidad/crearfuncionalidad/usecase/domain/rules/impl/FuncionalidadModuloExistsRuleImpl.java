package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadModuloExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
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
    public void execute(final CrearFuncionalidadDomain data) {

        if (data == null
                || UUIDHelper.getDefault().equals(data.getIdModulo())) {

            throw ValidationException.build(
                    "El modulo asociado a la funcionalidad es obligatorio.");
        }

        final ModuloEntity modulo = moduloRepository
                .findById(data.getIdModulo())
                .orElse(null);

        if (modulo == null
                || UUIDHelper.getDefault().equals(modulo.getId())) {

            throw NotFoundException.build(
                    "El modulo con el id "
                            + data.getIdModulo()
                            + " no existe en el sistema.");
        }
    }
}