package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.List;
import java.util.UUID;

/** Catalogo fijo de estados de metadatos por ambiente; no depende de una tabla persistida. */
public record EstadoMetadatoAmbienteCatalogoRecord(UUID id, String nombre) {

    public static final UUID ID_ACTIVO = UUID.fromString("00000000-0000-0000-0000-000000000020");
    public static final UUID ID_INACTIVO = UUID.fromString("00000000-0000-0000-0000-000000000021");

    private static final List<EstadoMetadatoAmbienteCatalogoRecord> VALORES = List.of(
            new EstadoMetadatoAmbienteCatalogoRecord(ID_ACTIVO, "Activo"),
            new EstadoMetadatoAmbienteCatalogoRecord(ID_INACTIVO, "Inactivo"));

    public static List<EstadoMetadatoAmbienteCatalogoRecord> valores() {
        return VALORES;
    }

    public EstadoMetadatoAmbienteEntity toEntity() {
        return EstadoMetadatoAmbienteEntity.create(id, nombre);
    }
}
