package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.interactor.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.CrearMetadatosTodosAmbientes;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.dto.CrearMetadatosTodosAmbientesDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.interactor.CrearMetadatosTodosAmbientesInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.interactor.mapper.CrearMetadatosTodosAmbientesDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;

@Service
public final class CrearMetadatosTodosAmbientesInteractorImpl implements CrearMetadatosTodosAmbientesInteractor {
    private final CrearMetadatosTodosAmbientes crear;
    public CrearMetadatosTodosAmbientesInteractorImpl(final CrearMetadatosTodosAmbientes crear) { this.crear = crear; }
    @Override public List<MetadatoEntity> execute(final CrearMetadatosTodosAmbientesDtoRequest data) {
        return crear.execute(CrearMetadatosTodosAmbientesDtoMapper.INSTANCE.toDomain(
                CrearMetadatosTodosAmbientesDtoMapper.INSTANCE.toDtoInput(data)));
    }
}
