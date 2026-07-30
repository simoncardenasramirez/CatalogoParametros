package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class ActualizarOrganizacionDto {

    private UUID id;
    private String nombre;

    public ActualizarOrganizacionDto() {
        setId(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
    }

    public ActualizarOrganizacionDto(final UUID id, final String nombre) {
        setId(id);
        setNombre(nombre);
    }

    public static ActualizarOrganizacionDto create(final UUID id, final String nombre) {
        return new ActualizarOrganizacionDto(id, nombre);
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
