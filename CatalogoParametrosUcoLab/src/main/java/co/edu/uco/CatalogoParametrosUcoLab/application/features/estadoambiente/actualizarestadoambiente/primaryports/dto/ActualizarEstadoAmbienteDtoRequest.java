package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.dto;

public final class ActualizarEstadoAmbienteDtoRequest {
    private String nombre;

    public ActualizarEstadoAmbienteDtoRequest() { }

    public ActualizarEstadoAmbienteDtoRequest(final String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }
}
