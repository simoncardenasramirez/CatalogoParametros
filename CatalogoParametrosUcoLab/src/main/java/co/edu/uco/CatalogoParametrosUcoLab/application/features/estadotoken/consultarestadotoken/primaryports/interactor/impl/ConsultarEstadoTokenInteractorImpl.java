package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadotoken.consultarestadotoken.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadotoken.consultarestadotoken.primaryports.interactor.ConsultarEstadoTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoTokenRepository;

@Service
public final class ConsultarEstadoTokenInteractorImpl implements ConsultarEstadoTokenInteractor {
    private final EstadoTokenRepository estadoTokenRepository;

    public ConsultarEstadoTokenInteractorImpl(final EstadoTokenRepository estadoTokenRepository) {
        this.estadoTokenRepository = estadoTokenRepository;
    }

    @Override
    public List<EstadoTokenEntity> execute() { return estadoTokenRepository.findAll(); }

    @Override
    public List<EstadoTokenEntity> execute(final UUID id) {
        return estadoTokenRepository.findById(id).map(List::of).orElse(List.of());
    }
}
