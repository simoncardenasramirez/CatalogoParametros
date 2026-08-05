package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarParametroDto {

    private String nombre;
    private String idFuncionalidad;
    private String idTipoParametro;
    private String activo;

    public ActualizarParametroDto() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, TextHelper.EMPTY, "true");
    }

    public ActualizarParametroDto(final String nombre, final String idFuncionalidad, final String idTipoParametro,
                                  final String activo) {
        setNombre(nombre);
        setIdFuncionalidad(idFuncionalidad);
        setIdTipoParametro(idTipoParametro);
        setActivo(activo);
    }

    public static ActualizarParametroDto create(final String nombre, final String idFuncionalidad,
                                                final String idTipoParametro, final String activo) {
        return new ActualizarParametroDto(nombre, idFuncionalidad, idTipoParametro, activo);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public String getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(final String idFuncionalidad) {
        this.idFuncionalidad = TextHelper.applyTrim(idFuncionalidad);
    }

    public String getIdTipoParametro() {
        return idTipoParametro;
    }

    public void setIdTipoParametro(final String idTipoParametro) {
        this.idTipoParametro = TextHelper.applyTrim(idTipoParametro);
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(final String activo) {
        this.activo = activo == null ? "true" : TextHelper.applyTrim(activo).toLowerCase();
    }
}
