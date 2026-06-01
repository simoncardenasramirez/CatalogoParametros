package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.ConsultarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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