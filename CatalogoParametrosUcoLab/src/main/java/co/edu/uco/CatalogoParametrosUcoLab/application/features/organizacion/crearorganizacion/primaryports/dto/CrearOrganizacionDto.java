package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearOrganizacionDto {

    private String nombre;

    public CrearOrganizacionDto() {
        this(TextHelper.EMPTY);
    }

    public CrearOrganizacionDto(final String nombre) {
        setNombre(nombre);
    }

    public static CrearOrganizacionDto create(final String nombre) {
        return new CrearOrganizacionDto(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
