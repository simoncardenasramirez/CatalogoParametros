package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.dto;

public final class ActualizarAmbienteDtoRequest {
    private String nombre;

    public ActualizarAmbienteDtoRequest() { }

    public ActualizarAmbienteDtoRequest(final String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }
}
