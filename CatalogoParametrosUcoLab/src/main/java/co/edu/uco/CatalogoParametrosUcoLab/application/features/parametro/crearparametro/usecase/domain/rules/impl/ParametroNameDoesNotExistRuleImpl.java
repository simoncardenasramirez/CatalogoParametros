package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameDoesNotExistRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class ParametroNameDoesNotExistRuleImpl implements ParametroNameDoesNotExistRule {

    private final ParametroRepository parametroRepository;

    public ParametroNameDoesNotExistRuleImpl(final ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public void execute(final CrearParametroDomain data) {
        if (parametroRepository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe un parametro con ese nombre.");
        }
    }
}
