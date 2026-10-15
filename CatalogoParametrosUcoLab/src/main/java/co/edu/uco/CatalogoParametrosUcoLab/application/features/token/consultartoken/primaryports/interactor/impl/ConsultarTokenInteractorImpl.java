package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultartoken.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultartoken.primaryports.interactor.ConsultarTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TokenRepository;

@Service
public final class ConsultarTokenInteractorImpl implements ConsultarTokenInteractor {
    private final TokenRepository repository;
    public ConsultarTokenInteractorImpl(final TokenRepository repository) { this.repository = repository; }
    @Override public List<TokenEntity> execute() { return repository.findAll(); }
    @Override public List<TokenEntity> execute(final UUID id) {
        return repository.findById(id).map(List::of).orElseGet(List::of);
    }
}
