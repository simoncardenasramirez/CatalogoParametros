package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearEstadoMetadatoAmbienteDtoInput {
    private String nombre;

    public CrearEstadoMetadatoAmbienteDtoInput() {
        this(TextHelper.EMPTY);
    }

    public CrearEstadoMetadatoAmbienteDtoInput(final String nombre) {
        setNombre(nombre);
    }

    public static CrearEstadoMetadatoAmbienteDtoInput create(final String nombre) {
        return new CrearEstadoMetadatoAmbienteDtoInput(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
