package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.consultarambiente.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.consultarambiente.primaryports.interactor.ConsultarAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ConsultarAmbienteInteractorImpl implements ConsultarAmbienteInteractor {
    private final AmbienteRepository repository;
    public ConsultarAmbienteInteractorImpl(final AmbienteRepository repository) { this.repository = repository; }

    @Override public AmbienteEntity execute(final UUID id) {
        return repository.findById(id).orElseThrow(() -> NotFoundException.build("No se encontro ambiente."));
    }
    @Override public List<AmbienteEntity> execute(final int pagina, final int tamanoPagina) {
        return repository.findAllPaginado(Math.max(pagina, 1), Math.max(tamanoPagina, 1));
    }
}
