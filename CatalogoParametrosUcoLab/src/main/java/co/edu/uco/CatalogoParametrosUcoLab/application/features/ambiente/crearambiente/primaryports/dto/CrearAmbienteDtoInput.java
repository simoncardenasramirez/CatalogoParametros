package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearAmbienteDtoInput {
    private String nombre;

    public CrearAmbienteDtoInput() {
        this(TextHelper.EMPTY);
    }

    public CrearAmbienteDtoInput(final String nombre) {
        setNombre(nombre);
    }

    public static CrearAmbienteDtoInput create(final String nombre) {
        return new CrearAmbienteDtoInput(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
