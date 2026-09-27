package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.consultarestadoambiente.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.consultarestadoambiente.primaryports.interactor.ConsultarEstadoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ConsultarEstadoAmbienteInteractorImpl implements ConsultarEstadoAmbienteInteractor {
    private final EstadoAmbienteRepository repository;
    public ConsultarEstadoAmbienteInteractorImpl(final EstadoAmbienteRepository repository) { this.repository = repository; }

    @Override public EstadoAmbienteEntity execute(final UUID id) {
        return repository.findById(id).orElseThrow(() -> NotFoundException.build("No se encontro estadoambiente."));
    }
    @Override public List<EstadoAmbienteEntity> execute(final int pagina, final int tamanoPagina) {
        return repository.findAllPaginado(Math.max(pagina, 1), Math.max(tamanoPagina, 1));
    }
}
