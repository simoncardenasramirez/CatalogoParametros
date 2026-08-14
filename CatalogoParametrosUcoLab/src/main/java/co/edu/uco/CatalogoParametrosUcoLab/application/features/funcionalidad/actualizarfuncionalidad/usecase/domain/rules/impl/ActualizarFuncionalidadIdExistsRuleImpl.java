package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarFuncionalidadIdExistsRuleImpl implements ActualizarFuncionalidadIdExistsRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final FuncionalidadRepository funcionalidadRepository;

    public ActualizarFuncionalidadIdExistsRuleImpl(final FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    @Override
    public void execute(final ActualizarFuncionalidadDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getId())) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-39"));
        }

        if (funcionalidadRepository.findById(data.getId()).isEmpty()) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-38"));
        }
    }
}