package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;


import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarOrganizacionDtoRequest {

    private String id;
    private String nombre;

    public ActualizarOrganizacionDtoRequest() {
        this(TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public ActualizarOrganizacionDtoRequest(final String id, final String nombre) {
        setId(id);
        setNombre(nombre);
    }

    public static ActualizarOrganizacionDtoRequest create(final String id, final String nombre) {
        return new ActualizarOrganizacionDtoRequest(id, nombre);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = TextHelper.applyTrim(id);
        validateId();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
        validateNombre();
    }

    private void validateId() {
        if (TextHelper.isBlank(id)) {
            throw ValidationException.build("El identificador de la organizacion es obligatorio.");
        }
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw ValidationException.build("El identificador de la organizacion no es valido. Valor recibido:");
        }
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
