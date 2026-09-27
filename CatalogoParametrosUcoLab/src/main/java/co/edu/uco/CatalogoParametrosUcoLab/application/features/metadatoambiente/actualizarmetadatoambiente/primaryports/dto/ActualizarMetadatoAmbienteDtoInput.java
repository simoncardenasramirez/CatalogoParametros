package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.dto;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class ActualizarMetadatoAmbienteDtoInput {
    private UUID idParametro;
    private UUID idAmbiente;
    private UUID idEstadoMetadatoAmbiente;

    public ActualizarMetadatoAmbienteDtoInput() {
        this(UUIDHelper.getDefault(), UUIDHelper.getDefault(), UUIDHelper.getDefault());
    }

    public ActualizarMetadatoAmbienteDtoInput(final UUID idParametro, final UUID idAmbiente, final UUID idEstado) {
        this.idParametro = UUIDHelper.getDefault(idParametro);
        this.idAmbiente = UUIDHelper.getDefault(idAmbiente);
        this.idEstadoMetadatoAmbiente = UUIDHelper.getDefault(idEstado);
    }

    public static ActualizarMetadatoAmbienteDtoInput create(final UUID idParametro, final UUID idAmbiente, final UUID idEstado) {
        return new ActualizarMetadatoAmbienteDtoInput(idParametro, idAmbiente, idEstado);
    }

    public UUID getIdParametro() { return idParametro; }
    public UUID getIdAmbiente() { return idAmbiente; }
    public UUID getIdEstadoMetadatoAmbiente() { return idEstadoMetadatoAmbiente; }
}
