package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameDoesNotExistRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;

@Service
public final class ParametroNameDoesNotExistRuleImpl implements ParametroNameDoesNotExistRule {

    private final ParametroRepository parametroRepository;

    public ParametroNameDoesNotExistRuleImpl(final ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public void execute(final ParametroDomain data) {
        if (parametroRepository.existsByNombre(data.getNombre())) {
            throw new ParametroException("Ya existe un parametro con ese nombre.");
        }
    }
}
