package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.EliminarFuncionalidadIsNotUsedByParametroRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class EliminarFuncionalidadIsNotUsedByParametroRuleImpl implements EliminarFuncionalidadIsNotUsedByParametroRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final ParametroRepository parametroRepository;

    public EliminarFuncionalidadIsNotUsedByParametroRuleImpl(final ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public void execute(final UUID data) {
        final var parametros = parametroRepository.findByIdFuncionalidad(data);
        if (parametros != null && !parametros.isEmpty()) {
            throw ConflictException.build(consultarMensajePort.consultarMensaje("MSG-59"));
        }
    }
}