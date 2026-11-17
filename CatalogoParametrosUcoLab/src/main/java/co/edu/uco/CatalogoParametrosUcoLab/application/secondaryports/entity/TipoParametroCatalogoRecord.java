package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.List;
import java.util.UUID;

/** Catalogo fijo de tipos de parametro; no depende de una tabla persistida. */
public record TipoParametroCatalogoRecord(UUID id, String nombre) {

    public static final UUID ID_TECNICO = UUID.fromString("00000000-0000-0000-0000-000000000001");
    public static final UUID ID_FUNCIONAL = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private static final List<TipoParametroCatalogoRecord> VALORES = List.of(
            new TipoParametroCatalogoRecord(ID_TECNICO, "Técnico"),
            new TipoParametroCatalogoRecord(ID_FUNCIONAL, "Funcional"));

    public static List<TipoParametroCatalogoRecord> valores() {
        return VALORES;
    }

    public TipoParametroEntity toEntity() {
        return TipoParametroEntity.create(id, nombre);
    }
}
