package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

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

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final ModuloRepository moduloRepository;

    public FuncionalidadModuloExistsRuleImpl(
            final ModuloRepository moduloRepository) {

        this.moduloRepository = moduloRepository;
    }

    @Override
    public void execute(final CrearFuncionalidadDomain data) {

        if (data == null
                || UUIDHelper.getDefault().equals(data.getIdModulo())) {

            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-53"));
        }

        final ModuloEntity modulo = moduloRepository
                .findById(data.getIdModulo())
                .orElse(null);

        if (modulo == null
                || UUIDHelper.getDefault().equals(modulo.getId())) {

            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-52"));
        }
    }
}