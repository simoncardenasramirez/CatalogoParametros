package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.rules.ParametroNameDoesNotExistRule;

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
