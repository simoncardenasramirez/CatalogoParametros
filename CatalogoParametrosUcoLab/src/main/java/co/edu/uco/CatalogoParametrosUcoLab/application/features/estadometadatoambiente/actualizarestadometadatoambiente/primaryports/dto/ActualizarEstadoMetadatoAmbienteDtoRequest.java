package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.dto;

public final class ActualizarEstadoMetadatoAmbienteDtoRequest {
    private String nombre;

    public ActualizarEstadoMetadatoAmbienteDtoRequest() { }

    public ActualizarEstadoMetadatoAmbienteDtoRequest(final String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }
}
