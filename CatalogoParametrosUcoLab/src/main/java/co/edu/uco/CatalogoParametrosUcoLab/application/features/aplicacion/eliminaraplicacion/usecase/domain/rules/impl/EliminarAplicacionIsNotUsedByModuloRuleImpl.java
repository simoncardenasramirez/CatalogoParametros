package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.EliminarAplicacionIsNotUsedByModuloRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class EliminarAplicacionIsNotUsedByModuloRuleImpl implements EliminarAplicacionIsNotUsedByModuloRule {

    private final ModuloRepository moduloRepository;

    public EliminarAplicacionIsNotUsedByModuloRuleImpl(final ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Override
    public void execute(final UUID id) {
        if (moduloRepository.existsByIdAplicacion(id)) {
            throw ConflictException.build(
                    "No se puede eliminar la aplicacion porque esta siendo usada por uno o mas modulos.");
        }
    }
}
