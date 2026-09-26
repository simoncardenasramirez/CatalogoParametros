package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.dto;

public final class CrearEstadoAmbienteDtoRequest {
    private String nombre;

    public CrearEstadoAmbienteDtoRequest() { }

    public CrearEstadoAmbienteDtoRequest(final String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }
}
