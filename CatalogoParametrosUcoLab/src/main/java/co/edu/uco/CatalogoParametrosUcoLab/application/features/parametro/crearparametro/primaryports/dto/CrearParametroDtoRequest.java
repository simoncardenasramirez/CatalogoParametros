package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearParametroDtoRequest {

    private String nombre;
    private String idFuncionalidad;
    private String idTipoParametro;
    private String activo;

    public CrearParametroDtoRequest() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, TextHelper.EMPTY, "true");
    }

    public CrearParametroDtoRequest(final String nombre, final String idFuncionalidad, final String idTipoParametro,
                             final String activo) {
        setNombre(nombre);
        setIdFuncionalidad(idFuncionalidad);
        setIdTipoParametro(idTipoParametro);
        setActivo(activo);
    }

    public static CrearParametroDtoRequest create(final String nombre, final String idFuncionalidad, final String idTipoParametro,
                                           final String activo) {
        return new CrearParametroDtoRequest(nombre, idFuncionalidad, idTipoParametro, activo);
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
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-128"));
        }
        if (nombre.length() < 3 || nombre.length() > 50) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-127"));
        }
    }

    private void validateIdFuncionalidad() {
        if (TextHelper.isBlank(idFuncionalidad)) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-126"));
        }
        try {
            UUID.fromString(idFuncionalidad);
        } catch (IllegalArgumentException e) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-125"));
        }
    }

    private void validateIdTipoParametro() {
        if (TextHelper.isBlank(idTipoParametro)) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-124"));
        }
        try {
            UUID.fromString(idTipoParametro);
        } catch (IllegalArgumentException e) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-123"));
        }
    }

    private void validateActivo() {
        if (!"true".equals(activo) && !"false".equals(activo)) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-122"));
        }
    }
}
