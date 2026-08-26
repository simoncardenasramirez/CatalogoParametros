package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.CrearMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearMetadatoDtoMapper {
    INSTANCE;
    public CrearMetadatoDtoInput toDtoInput(final CrearMetadatoDtoRequest dto) {
        return CrearMetadatoDtoInput.create(UUID.fromString(dto.getIdParametro()),
                UUID.fromString(dto.getIdTipoMetadato()), dto.getValor());
    }
    public CrearMetadatoDomain toDomain(final CrearMetadatoDtoInput input) {
        return CrearMetadatoDomain.create(UUIDHelper.getDefault(), input.getIdParametro(), input.getIdTipoMetadato(),
                input.getValor());
    }
}
