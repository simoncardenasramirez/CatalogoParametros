package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearOrganizacionDtoInput {

    private String nombre;

    public CrearOrganizacionDtoInput() {
        this(TextHelper.EMPTY);
    }

    public CrearOrganizacionDtoInput(final String nombre) {
        setNombre(nombre);
    }

    public static CrearOrganizacionDtoInput create(final String nombre) {
        return new CrearOrganizacionDtoInput(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
