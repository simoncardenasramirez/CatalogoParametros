package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class AmbienteEntity {
    private UUID id;
    private String nombre;
    private boolean activo;

    private AmbienteEntity(final UUID id, final String nombre, final boolean activo) {
        this.id = UUIDHelper.getDefault(id);
        this.nombre = TextHelper.applyTrim(nombre);
        this.activo = activo;
    }

    public static AmbienteEntity create(final UUID id, final String nombre) {
        return create(id, nombre, true);
    }

    public static AmbienteEntity create(final UUID id, final String nombre, final boolean activo) {
        return new AmbienteEntity(id, nombre, activo);
    }

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = UUIDHelper.getDefault(id); }
    public String getNombre() { return nombre; }
    public void setNombre(final String nombre) { this.nombre = TextHelper.applyTrim(nombre); }
    public boolean isActivo() { return activo; }
    public void setActivo(final boolean activo) { this.activo = activo; }
}
