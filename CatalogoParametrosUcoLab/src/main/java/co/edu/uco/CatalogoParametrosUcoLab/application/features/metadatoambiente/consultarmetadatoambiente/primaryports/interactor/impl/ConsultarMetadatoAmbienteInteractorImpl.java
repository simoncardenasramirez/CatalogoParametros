package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.consultarmetadatoambiente.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.consultarmetadatoambiente.primaryports.interactor.ConsultarMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ConsultarMetadatoAmbienteInteractorImpl implements ConsultarMetadatoAmbienteInteractor {
    private final MetadatoAmbienteRepository repository;
    public ConsultarMetadatoAmbienteInteractorImpl(final MetadatoAmbienteRepository repository) { this.repository = repository; }

    @Override public MetadatoAmbienteEntity execute(final UUID id) {
        return repository.findById(id).orElseThrow(() -> NotFoundException.build("No se encontro metadatoambiente."));
    }
    @Override public List<MetadatoAmbienteEntity> execute(final int pagina, final int tamanoPagina) {
        return repository.findAllPaginado(Math.max(pagina, 1), Math.max(tamanoPagina, 1));
    }
}
