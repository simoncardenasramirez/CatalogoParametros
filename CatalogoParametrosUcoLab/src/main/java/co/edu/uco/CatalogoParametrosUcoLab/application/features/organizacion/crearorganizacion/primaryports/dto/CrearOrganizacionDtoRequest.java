package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto;


import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

public final class CrearOrganizacionDtoRequest {

    private String nombre;

    public CrearOrganizacionDtoRequest() {
        this(TextHelper.EMPTY);
    }

    public CrearOrganizacionDtoRequest(final String nombre) {
        setNombre(nombre);
    }

    public static CrearOrganizacionDtoRequest create(final String nombre) {
        return new CrearOrganizacionDtoRequest(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
        ValidateHelper.validateNombre(this.nombre, "de la organizacion");
    }
}
