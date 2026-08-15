package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadModuloExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarFuncionalidadModuloExistsRuleImpl implements ActualizarFuncionalidadModuloExistsRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final ModuloRepository moduloRepository;

    public ActualizarFuncionalidadModuloExistsRuleImpl(final ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Override
    public void execute(final ActualizarFuncionalidadDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getIdModulo())) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-41"));
        }

        final ModuloEntity modulo = moduloRepository.findById(data.getIdModulo()).orElse(null);

        if (modulo == null || UUIDHelper.getDefault().equals(modulo.getId())) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-40"));
        }
    }
}