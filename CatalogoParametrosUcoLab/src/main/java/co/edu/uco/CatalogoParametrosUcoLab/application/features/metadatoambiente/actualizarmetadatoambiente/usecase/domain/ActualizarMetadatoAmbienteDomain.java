package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class ActualizarMetadatoAmbienteDomain extends Domain {
    private final UUID idParametro;
    private final UUID idAmbiente;
    private final UUID idEstadoMetadatoAmbiente;

    private ActualizarMetadatoAmbienteDomain(final UUID id, final UUID idParametro, final UUID idAmbiente, final UUID idEstado) {
        super(id);
        this.idParametro = UUIDHelper.getDefault(idParametro);
        this.idAmbiente = UUIDHelper.getDefault(idAmbiente);
        this.idEstadoMetadatoAmbiente = UUIDHelper.getDefault(idEstado);
    }

    public static ActualizarMetadatoAmbienteDomain create(final UUID id, final UUID idParametro,
            final UUID idAmbiente, final UUID idEstado) {
        return new ActualizarMetadatoAmbienteDomain(id, idParametro, idAmbiente, idEstado);
    }

    public UUID getIdParametro() { return idParametro; }
    public UUID getIdAmbiente() { return idAmbiente; }
    public UUID getIdEstadoMetadatoAmbiente() { return idEstadoMetadatoAmbiente; }
}
