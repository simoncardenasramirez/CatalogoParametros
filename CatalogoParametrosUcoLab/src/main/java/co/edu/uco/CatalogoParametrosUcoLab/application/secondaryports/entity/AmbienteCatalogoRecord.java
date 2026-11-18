package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.List;
import java.util.UUID;

/** Catalogo fijo de ambientes; no depende de una tabla persistida. */
public record AmbienteCatalogoRecord(UUID id, String nombre) {

    public static final UUID ID_PRODUCCION = UUID.fromString("00000000-0000-0000-0000-000000000010");
    public static final UUID ID_DESARROLLO = UUID.fromString("00000000-0000-0000-0000-000000000011");
    public static final UUID ID_PRUEBAS = UUID.fromString("00000000-0000-0000-0000-000000000012");

    private static final List<AmbienteCatalogoRecord> VALORES = List.of(
            new AmbienteCatalogoRecord(ID_PRODUCCION, "Producción"),
            new AmbienteCatalogoRecord(ID_DESARROLLO, "Desarrollo"),
            new AmbienteCatalogoRecord(ID_PRUEBAS, "Pruebas"));

    public static List<AmbienteCatalogoRecord> valores() {
        return VALORES;
    }

    public AmbienteEntity toEntity() {
        return AmbienteEntity.create(id, nombre);
    }
}
