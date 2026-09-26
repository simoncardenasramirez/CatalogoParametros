package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.dto;

public final class CrearEstadoMetadatoAmbienteDtoRequest {
    private String nombre;

    public CrearEstadoMetadatoAmbienteDtoRequest() { }

    public CrearEstadoMetadatoAmbienteDtoRequest(final String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }
}
