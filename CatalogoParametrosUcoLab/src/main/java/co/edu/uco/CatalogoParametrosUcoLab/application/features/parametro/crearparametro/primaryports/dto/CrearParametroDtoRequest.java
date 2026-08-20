package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto;


import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

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
