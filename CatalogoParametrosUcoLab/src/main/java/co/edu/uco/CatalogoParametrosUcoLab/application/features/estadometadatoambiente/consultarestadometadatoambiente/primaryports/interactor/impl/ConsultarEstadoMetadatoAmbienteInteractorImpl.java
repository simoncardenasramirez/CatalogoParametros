package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.consultarestadometadatoambiente.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.consultarestadometadatoambiente.primaryports.interactor.ConsultarEstadoMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ConsultarEstadoMetadatoAmbienteInteractorImpl implements ConsultarEstadoMetadatoAmbienteInteractor {
    private final EstadoMetadatoAmbienteRepository repository;
    public ConsultarEstadoMetadatoAmbienteInteractorImpl(final EstadoMetadatoAmbienteRepository repository) { this.repository = repository; }

    @Override public EstadoMetadatoAmbienteEntity execute(final UUID id) {
        return repository.findById(id).orElseThrow(() -> NotFoundException.build("No se encontro estadometadatoambiente."));
    }
    @Override public List<EstadoMetadatoAmbienteEntity> execute(final int pagina, final int tamanoPagina) {
        return repository.findAllPaginado(Math.max(pagina, 1), Math.max(tamanoPagina, 1));
    }
}
