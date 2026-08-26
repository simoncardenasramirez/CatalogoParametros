package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.CrearMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.MetadatoParametroExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class MetadatoParametroExistsRuleImpl implements MetadatoParametroExistsRule {
    private final ParametroRepository parametroRepository;
    public MetadatoParametroExistsRuleImpl(final ParametroRepository repository) { parametroRepository = repository; }
    @Override public void execute(final CrearMetadatoDomain data) {
        if (parametroRepository.findById(data.getIdParametro()).isEmpty())
            throw NotFoundException.build("El parametro asociado al metadato no existe.");
    }
}
