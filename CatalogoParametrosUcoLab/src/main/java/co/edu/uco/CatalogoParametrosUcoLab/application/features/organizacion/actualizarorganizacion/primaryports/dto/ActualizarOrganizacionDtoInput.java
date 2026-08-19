package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;

public final class ActualizarOrganizacionDtoInput {

    private String nombre;

    public ActualizarOrganizacionDtoInput() {
        this("");
    }

    public ActualizarOrganizacionDtoInput(final String nombre) {
        setNombre(nombre);
    }

    public static ActualizarOrganizacionDtoInput create(final String nombre) {
        return new ActualizarOrganizacionDtoInput(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }
}
