package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearMetadatoAmbienteDomain extends Domain {
    private final UUID idParametro;
    private final UUID idAmbiente;
    private final UUID idEstadoMetadatoAmbiente;

    private CrearMetadatoAmbienteDomain(final UUID id, final UUID idParametro, final UUID idAmbiente, final UUID idEstado) {
        super(id);
        this.idParametro = UUIDHelper.getDefault(idParametro);
        this.idAmbiente = UUIDHelper.getDefault(idAmbiente);
        this.idEstadoMetadatoAmbiente = UUIDHelper.getDefault(idEstado);
    }

    public static CrearMetadatoAmbienteDomain create(final UUID id, final UUID idParametro,
            final UUID idAmbiente, final UUID idEstado) {
        return new CrearMetadatoAmbienteDomain(id, idParametro, idAmbiente, idEstado);
    }

    public UUID getIdParametro() { return idParametro; }
    public UUID getIdAmbiente() { return idAmbiente; }
    public UUID getIdEstadoMetadatoAmbiente() { return idEstadoMetadatoAmbiente; }
}
