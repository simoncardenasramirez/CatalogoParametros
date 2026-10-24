package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor.mapper;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.domain.ActualizarMetadatoDomain;
public enum ActualizarMetadatoDtoMapper {
    INSTANCE;
    private UUID ambiente(final String valor) {
        co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper.validateId(valor, "identificador del ambiente");
        return UUID.fromString(valor);
    }
    public ActualizarMetadatoDtoInput toDtoInput(final ActualizarMetadatoDtoRequest dto) {
        return new ActualizarMetadatoDtoInput(UUID.fromString(dto.getIdParametro()), UUID.fromString(dto.getIdTipoMetadato()), ambiente(dto.getIdAmbiente()), dto.getValor());
    }
    public ActualizarMetadatoDomain toDomain(final UUID id, final ActualizarMetadatoDtoInput input) {
        return ActualizarMetadatoDomain.create(id, input.idParametro(), input.idTipoMetadato(), input.idAmbiente(), input.valor());
    }
}
