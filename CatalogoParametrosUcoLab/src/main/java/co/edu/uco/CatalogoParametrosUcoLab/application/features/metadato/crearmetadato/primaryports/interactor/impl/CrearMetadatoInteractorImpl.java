package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.CrearMetadato;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.CrearMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.mapper.CrearMetadatoDtoMapper;

@Service
public final class CrearMetadatoInteractorImpl implements CrearMetadatoInteractor {
    private final CrearMetadato crearMetadato;
    public CrearMetadatoInteractorImpl(final CrearMetadato crearMetadato) { this.crearMetadato = crearMetadato; }
    @Override
    public void execute(final CrearMetadatoDtoRequest data) {
        crearMetadato.execute(CrearMetadatoDtoMapper.INSTANCE.toDomain(CrearMetadatoDtoMapper.INSTANCE.toDtoInput(data)));
    }
}
