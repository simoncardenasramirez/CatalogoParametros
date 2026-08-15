package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class ActualizarFuncionalidadNombreDoesNotExistRuleImpl implements ActualizarFuncionalidadNombreDoesNotExistRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final FuncionalidadRepository funcionalidadRepository;

    public ActualizarFuncionalidadNombreDoesNotExistRuleImpl(final FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    @Override
    public void execute(final ActualizarFuncionalidadDomain data) {
        final var existingFuncionalidad = funcionalidadRepository.findById(data.getId());
        if (existingFuncionalidad.isPresent()) {
            final var funcionalidad = existingFuncionalidad.get();
            if (!funcionalidad.getNombre().equals(data.getNombre())
                    && funcionalidadRepository.existsByNombre(data.getNombre())) {
                throw ConflictException.build(consultarMensajePort.consultarMensaje("MSG-42"));
            }
        }
    }
}