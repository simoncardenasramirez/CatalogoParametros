package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class OrganizacionEntity {

    private UUID id;
    private String nombre;

    private OrganizacionEntity() {
        setId(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
    }

    private OrganizacionEntity(final UUID id, final String nombre) {
        setId(id);
        setNombre(nombre);
    }

    public static OrganizacionEntity create(final UUID id, final String nombre) {
        return new OrganizacionEntity(id, nombre);
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = UUIDHelper.getDefault(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
