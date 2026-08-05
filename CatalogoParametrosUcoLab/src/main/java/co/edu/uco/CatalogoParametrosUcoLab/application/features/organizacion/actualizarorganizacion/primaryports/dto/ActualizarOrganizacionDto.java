package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarOrganizacionDto {

    private String id;
    private String nombre;

    public ActualizarOrganizacionDto() {
        this(TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public ActualizarOrganizacionDto(final String id, final String nombre) {
        setId(id);
        setNombre(nombre);
    }

    public static ActualizarOrganizacionDto create(final String id, final String nombre) {
        return new ActualizarOrganizacionDto(id, nombre);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = TextHelper.applyTrim(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
