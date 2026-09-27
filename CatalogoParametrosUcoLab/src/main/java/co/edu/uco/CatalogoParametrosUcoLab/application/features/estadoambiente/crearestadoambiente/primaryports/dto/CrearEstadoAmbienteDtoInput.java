package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearEstadoAmbienteDtoInput {
    private String nombre;

    public CrearEstadoAmbienteDtoInput() {
        this(TextHelper.EMPTY);
    }

    public CrearEstadoAmbienteDtoInput(final String nombre) {
        setNombre(nombre);
    }

    public static CrearEstadoAmbienteDtoInput create(final String nombre) {
        return new CrearEstadoAmbienteDtoInput(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
