package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarAmbienteDtoInput {
    private String nombre;

    public ActualizarAmbienteDtoInput() {
        this(TextHelper.EMPTY);
    }

    public ActualizarAmbienteDtoInput(final String nombre) {
        setNombre(nombre);
    }

    public static ActualizarAmbienteDtoInput create(final String nombre) {
        return new ActualizarAmbienteDtoInput(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
