package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.CrearMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearMetadatoDtoMapper {
    INSTANCE;
    private UUID ambiente(final String valor) {
        co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper.validateId(valor, "identificador del ambiente");
        return UUID.fromString(valor);
    }
    public CrearMetadatoDtoInput toDtoInput(final CrearMetadatoDtoRequest dto) {
        return CrearMetadatoDtoInput.create(UUID.fromString(dto.getIdParametro()),
                UUID.fromString(dto.getIdTipoMetadato()), ambiente(dto.getIdAmbiente()), dto.getValor());
    }
    public CrearMetadatoDomain toDomain(final CrearMetadatoDtoInput input) {
        return CrearMetadatoDomain.create(UUIDHelper.getDefault(), input.getIdParametro(), input.getIdTipoMetadato(), input.getIdAmbiente(),
                input.getValor());
    }
}
