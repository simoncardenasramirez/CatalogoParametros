package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.parametro.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.parametro.ConsultarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;

@Service
public class ConsultarParametroInteractorImpl implements ConsultarParametroInteractor {

    private final ParametroRepository parametroRepository;

    public ConsultarParametroInteractorImpl(final ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public List<ParametroEntity> execute() {
        return parametroRepository.findAll();
    }

    @Override
    public List<ParametroEntity> execute(final UUID id) {
        return parametroRepository.findById(id)
                .map(List::of)
                .orElse(List.of());
    }
}