package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;


import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarOrganizacionDtoRequest {

    private String nombre;

    public ActualizarOrganizacionDtoRequest() {
        this(TextHelper.EMPTY);
    }

    public ActualizarOrganizacionDtoRequest(final String nombre) {
        setNombre(nombre);
    }

    public static ActualizarOrganizacionDtoRequest create(final String nombre) {
        return new ActualizarOrganizacionDtoRequest(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
        validateNombre();
    }

    private void validateNombre() {
        if (TextHelper.isBlank(nombre)) {
            throw ValidationException.build("El nombre de la organizacion es obligatorio.");
        }
        if (nombre.length() < 3 || nombre.length() > 50) {
            throw ValidationException.build("El nombre debe tener entre 3 y 50 caracteres.");
        }
    }
}
