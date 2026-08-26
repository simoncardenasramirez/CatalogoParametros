package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.CrearMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.MetadatoTipoMetadatoExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class MetadatoTipoMetadatoExistsRuleImpl implements MetadatoTipoMetadatoExistsRule {
    private final TipoMetadatoRepository repository;
    public MetadatoTipoMetadatoExistsRuleImpl(final TipoMetadatoRepository repository) { this.repository = repository; }
    @Override public void execute(final CrearMetadatoDomain data) {
        if (repository.findById(data.getIdTipoMetadato()).isEmpty())
            throw NotFoundException.build("El tipo de metadato no existe.");
    }
}
