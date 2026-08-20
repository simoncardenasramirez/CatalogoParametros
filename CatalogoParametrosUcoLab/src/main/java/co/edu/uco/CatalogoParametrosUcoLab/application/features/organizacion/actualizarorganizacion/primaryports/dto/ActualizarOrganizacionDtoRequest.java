package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;


import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

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
        ValidateHelper.validateNombre(this.nombre, "de la organizacion");
    }
}
