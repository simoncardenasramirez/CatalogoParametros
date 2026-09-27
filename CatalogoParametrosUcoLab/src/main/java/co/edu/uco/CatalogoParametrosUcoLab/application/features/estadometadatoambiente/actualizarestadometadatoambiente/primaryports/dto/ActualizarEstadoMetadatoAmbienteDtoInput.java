package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarEstadoMetadatoAmbienteDtoInput {
    private String nombre;

    public ActualizarEstadoMetadatoAmbienteDtoInput() {
        this(TextHelper.EMPTY);
    }

    public ActualizarEstadoMetadatoAmbienteDtoInput(final String nombre) {
        setNombre(nombre);
    }

    public static ActualizarEstadoMetadatoAmbienteDtoInput create(final String nombre) {
        return new ActualizarEstadoMetadatoAmbienteDtoInput(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
