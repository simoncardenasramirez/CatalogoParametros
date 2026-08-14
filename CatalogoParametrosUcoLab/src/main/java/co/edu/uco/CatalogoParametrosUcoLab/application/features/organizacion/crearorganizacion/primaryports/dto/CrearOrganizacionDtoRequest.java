package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

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
        validateNombre();
    }

    private void validateNombre() {
        if (TextHelper.isBlank(nombre)) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-100"));
        }
        if (nombre.length() < 3 || nombre.length() > 50) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-99"));
        }
    }
}
