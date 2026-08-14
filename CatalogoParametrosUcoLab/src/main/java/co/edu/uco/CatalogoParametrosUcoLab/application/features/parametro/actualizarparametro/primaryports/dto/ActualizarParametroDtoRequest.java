package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto;


import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarParametroDtoRequest {

    private String nombre;
    private String idFuncionalidad;
    private String idTipoParametro;
    private String activo;

    public ActualizarParametroDtoRequest() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, TextHelper.EMPTY, "true");
    }

    public ActualizarParametroDtoRequest(final String nombre, final String idFuncionalidad, final String idTipoParametro,
                                  final String activo) {
        setNombre(nombre);
        setIdFuncionalidad(idFuncionalidad);
        setIdTipoParametro(idTipoParametro);
        setActivo(activo);
    }

    public static ActualizarParametroDtoRequest create(final String nombre, final String idFuncionalidad,
                                                final String idTipoParametro, final String activo) {
        return new ActualizarParametroDtoRequest(nombre, idFuncionalidad, idTipoParametro, activo);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
        validateNombre();
    }

    public String getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(final String idFuncionalidad) {
        this.idFuncionalidad = TextHelper.applyTrim(idFuncionalidad);
        validateIdFuncionalidad();
    }

    public String getIdTipoParametro() {
        return idTipoParametro;
    }

    public void setIdTipoParametro(final String idTipoParametro) {
        this.idTipoParametro = TextHelper.applyTrim(idTipoParametro);
        validateIdTipoParametro();
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(final String activo) {
        this.activo = activo == null ? "true" : TextHelper.applyTrim(activo).toLowerCase();
        validateActivo();
    }

    private void validateNombre() {
        if (TextHelper.isBlank(nombre)) {
            throw ValidationException.build("El nombre del parametro es obligatorio.");
        }
        if (nombre.length() < 3 || nombre.length() > 50) {
            throw ValidationException.build("El nombre debe tener entre 3 y 50 caracteres.");
        }
    }

    private void validateIdFuncionalidad() {
        if (TextHelper.isBlank(idFuncionalidad)) {
            throw ValidationException.build("El identificador de la funcionalidad es obligatorio.");
        }
        try {
            UUID.fromString(idFuncionalidad);
        } catch (IllegalArgumentException e) {
            throw ValidationException.build("El identificador de la funcionalidad no es valido. Valor recibido:");
        }
    }

    private void validateIdTipoParametro() {
        if (TextHelper.isBlank(idTipoParametro)) {
            throw ValidationException.build("El identificador del tipo de parametro es obligatorio.");
        }
        try {
            UUID.fromString(idTipoParametro);
        } catch (IllegalArgumentException e) {
            throw ValidationException.build("El identificador del tipo de parametro no es valido. Valor recibido:");
        }
    }

    private void validateActivo() {
        if (!"true".equals(activo) && !"false".equals(activo)) {
            throw ValidationException.build("El estado activo debe ser 'true' o 'false'. Valor recibido:");
        }
    }
}
