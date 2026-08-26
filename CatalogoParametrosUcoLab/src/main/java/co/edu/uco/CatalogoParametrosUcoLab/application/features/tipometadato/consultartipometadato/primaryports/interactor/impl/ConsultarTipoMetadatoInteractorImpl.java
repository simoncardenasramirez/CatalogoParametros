package co.edu.uco.CatalogoParametrosUcoLab.application.features.tipometadato.consultartipometadato.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.tipometadato.consultartipometadato.primaryports.interactor.ConsultarTipoMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoMetadatoRepository;

@Service
public final class ConsultarTipoMetadatoInteractorImpl implements ConsultarTipoMetadatoInteractor {
    private final TipoMetadatoRepository tipoMetadatoRepository;

    public ConsultarTipoMetadatoInteractorImpl(final TipoMetadatoRepository tipoMetadatoRepository) {
        this.tipoMetadatoRepository = tipoMetadatoRepository;
    }

    @Override
    public List<TipoMetadatoEntity> execute() { return tipoMetadatoRepository.findAll(); }

    @Override
    public List<TipoMetadatoEntity> execute(final UUID id) {
        return tipoMetadatoRepository.findById(id).map(List::of).orElse(List.of());
    }
}
