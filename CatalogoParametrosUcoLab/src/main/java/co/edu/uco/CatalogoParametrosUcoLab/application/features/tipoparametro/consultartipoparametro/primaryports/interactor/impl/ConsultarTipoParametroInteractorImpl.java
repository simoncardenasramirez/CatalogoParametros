package co.edu.uco.CatalogoParametrosUcoLab.application.features.tipoparametro.consultartipoparametro.primaryports.interactor.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.tipoparametro.consultartipoparametro.primaryports.interactor.ConsultarTipoParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConsultarTipoParametroInteractorImpl implements ConsultarTipoParametroInteractor {

    private final TipoParametroRepository tipoParametroRepository;

    public ConsultarTipoParametroInteractorImpl(final TipoParametroRepository tipoParametroRepository) {
        this.tipoParametroRepository = tipoParametroRepository;
    }

    @Override
    public List<TipoParametroEntity> execute() {
        return tipoParametroRepository.findAll();
    }

    @Override
    public List<TipoParametroEntity> execute(final UUID id) {
        return tipoParametroRepository.findById(id)
                .map(List::of)
                .orElse(List.of());
    }
}
