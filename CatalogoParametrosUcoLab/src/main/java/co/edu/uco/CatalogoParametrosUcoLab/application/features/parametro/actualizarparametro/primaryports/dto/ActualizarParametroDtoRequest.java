package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto;


import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

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
        ValidateHelper.validateNombre(this.nombre, "del parametro");
    }

    public String getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(final String idFuncionalidad) {
        this.idFuncionalidad = TextHelper.applyTrim(idFuncionalidad);
        ValidateHelper.validateId(this.idFuncionalidad, "identificador de la funcionalidad");
    }

    public String getIdTipoParametro() {
        return idTipoParametro;
    }

    public void setIdTipoParametro(final String idTipoParametro) {
        this.idTipoParametro = TextHelper.applyTrim(idTipoParametro);
        ValidateHelper.validateId(this.idTipoParametro, "identificador del tipo de parametro");
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(final String activo) {
        this.activo = activo == null ? "true" : TextHelper.applyTrim(activo).toLowerCase();
        ValidateHelper.validateActivo(this.activo);
    }
}
