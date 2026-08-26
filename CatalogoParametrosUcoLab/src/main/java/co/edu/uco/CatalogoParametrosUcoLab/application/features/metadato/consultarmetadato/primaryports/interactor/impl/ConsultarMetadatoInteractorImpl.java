package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.consultarmetadato.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.consultarmetadato.primaryports.interactor.ConsultarMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;

@Service
public final class ConsultarMetadatoInteractorImpl implements ConsultarMetadatoInteractor {
    private final MetadatoRepository repository;
    public ConsultarMetadatoInteractorImpl(final MetadatoRepository repository) { this.repository = repository; }
    @Override public List<MetadatoEntity> execute() { return repository.findAll(); }
    @Override public List<MetadatoEntity> execute(final UUID id) { return repository.findById(id).map(List::of).orElse(List.of()); }
    @Override public List<MetadatoEntity> executeByParametro(final UUID id) { return repository.findByIdParametro(id); }
}
