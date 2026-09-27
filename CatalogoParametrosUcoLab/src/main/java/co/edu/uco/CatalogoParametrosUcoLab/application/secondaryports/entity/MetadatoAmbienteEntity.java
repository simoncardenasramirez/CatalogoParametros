package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class MetadatoAmbienteEntity {
    private UUID id;
    private UUID idParametro;
    private UUID idAmbiente;
    private UUID idEstadoMetadatoAmbiente;

    private MetadatoAmbienteEntity(final UUID id, final UUID idParametro, final UUID idAmbiente,
            final UUID idEstadoMetadatoAmbiente) {
        setId(id);
        setIdParametro(idParametro);
        setIdAmbiente(idAmbiente);
        setIdEstadoMetadatoAmbiente(idEstadoMetadatoAmbiente);
    }

    public static MetadatoAmbienteEntity create(final UUID id, final UUID idParametro, final UUID idAmbiente,
            final UUID idEstadoMetadatoAmbiente) {
        return new MetadatoAmbienteEntity(id, idParametro, idAmbiente, idEstadoMetadatoAmbiente);
    }

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = UUIDHelper.getDefault(id); }
    public UUID getIdParametro() { return idParametro; }
    public void setIdParametro(final UUID idParametro) { this.idParametro = UUIDHelper.getDefault(idParametro); }
    public UUID getIdAmbiente() { return idAmbiente; }
    public void setIdAmbiente(final UUID idAmbiente) { this.idAmbiente = UUIDHelper.getDefault(idAmbiente); }
    public UUID getIdEstadoMetadatoAmbiente() { return idEstadoMetadatoAmbiente; }
    public void setIdEstadoMetadatoAmbiente(final UUID idEstadoMetadatoAmbiente) {
        this.idEstadoMetadatoAmbiente = UUIDHelper.getDefault(idEstadoMetadatoAmbiente);
    }
}
