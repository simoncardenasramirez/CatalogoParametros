package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.EliminarFuncionalidadIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class EliminarFuncionalidadIdExistsRuleImpl implements EliminarFuncionalidadIdExistsRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final FuncionalidadRepository funcionalidadRepository;

    public EliminarFuncionalidadIdExistsRuleImpl(final FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    @Override
    public void execute(final UUID data) {
        if (data == null || UUIDHelper.getDefault().equals(data)) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-58"));
        }

        if (funcionalidadRepository.findById(data).isEmpty()) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-57"));
        }
    }
}