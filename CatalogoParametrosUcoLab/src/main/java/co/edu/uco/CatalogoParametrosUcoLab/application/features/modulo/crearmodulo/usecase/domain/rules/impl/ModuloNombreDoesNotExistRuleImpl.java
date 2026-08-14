package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreDoesNotExistRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class ModuloNombreDoesNotExistRuleImpl implements ModuloNombreDoesNotExistRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final ModuloRepository moduloRepository;

    public ModuloNombreDoesNotExistRuleImpl(final ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Override
    public void execute(final CrearModuloDomain data) {
        if (moduloRepository.existsByNombre(data.getNombre())) {
            throw ConflictException.build(consultarMensajePort.consultarMensaje("MSG-86"));
        }
    }
}
