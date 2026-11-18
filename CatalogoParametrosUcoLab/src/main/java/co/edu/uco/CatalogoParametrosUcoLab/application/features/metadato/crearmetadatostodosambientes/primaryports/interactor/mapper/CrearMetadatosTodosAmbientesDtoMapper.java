package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.interactor.mapper;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.dto.CrearMetadatosTodosAmbientesDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.dto.CrearMetadatosTodosAmbientesDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.domain.CrearMetadatosTodosAmbientesDomain;

public enum CrearMetadatosTodosAmbientesDtoMapper {
    INSTANCE;
    public CrearMetadatosTodosAmbientesDtoInput toDtoInput(final CrearMetadatosTodosAmbientesDtoRequest dto) {
        return CrearMetadatosTodosAmbientesDtoInput.create(UUID.fromString(dto.getIdParametro()), UUID.fromString(dto.getIdTipoMetadato()), dto.getValor());
    }
    public CrearMetadatosTodosAmbientesDomain toDomain(final CrearMetadatosTodosAmbientesDtoInput input) {
        return CrearMetadatosTodosAmbientesDomain.create(input.getIdParametro(), input.getIdTipoMetadato(), input.getValor());
    }
}
