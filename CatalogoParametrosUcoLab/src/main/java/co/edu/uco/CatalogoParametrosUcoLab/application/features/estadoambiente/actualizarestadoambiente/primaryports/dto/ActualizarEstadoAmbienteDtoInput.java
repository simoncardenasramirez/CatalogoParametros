package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarEstadoAmbienteDtoInput {
    private String nombre;

    public ActualizarEstadoAmbienteDtoInput() {
        this(TextHelper.EMPTY);
    }

    public ActualizarEstadoAmbienteDtoInput(final String nombre) {
        setNombre(nombre);
    }

    public static ActualizarEstadoAmbienteDtoInput create(final String nombre) {
        return new ActualizarEstadoAmbienteDtoInput(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
