package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearMetadatoAmbienteDtoInput {
    private UUID idParametro;
    private UUID idAmbiente;
    private UUID idEstadoMetadatoAmbiente;

    public CrearMetadatoAmbienteDtoInput() {
        this(UUIDHelper.getDefault(), UUIDHelper.getDefault(), UUIDHelper.getDefault());
    }

    public CrearMetadatoAmbienteDtoInput(final UUID idParametro, final UUID idAmbiente, final UUID idEstado) {
        this.idParametro = UUIDHelper.getDefault(idParametro);
        this.idAmbiente = UUIDHelper.getDefault(idAmbiente);
        this.idEstadoMetadatoAmbiente = UUIDHelper.getDefault(idEstado);
    }

    public static CrearMetadatoAmbienteDtoInput create(final UUID idParametro, final UUID idAmbiente, final UUID idEstado) {
        return new CrearMetadatoAmbienteDtoInput(idParametro, idAmbiente, idEstado);
    }

    public UUID getIdParametro() { return idParametro; }
    public UUID getIdAmbiente() { return idAmbiente; }
    public UUID getIdEstadoMetadatoAmbiente() { return idEstadoMetadatoAmbiente; }
}
