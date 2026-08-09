package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class ActualizarParametroDtoInput {

    private String nombre;
    private UUID idFuncionalidad;
    private UUID idTipoParametro;
    private boolean activo;

    public ActualizarParametroDtoInput() {
        this("", UUIDHelper.getDefault(), UUIDHelper.getDefault(), false);
    }

    public ActualizarParametroDtoInput(final String nombre, final UUID idFuncionalidad, final UUID idTipoParametro,
                                       final boolean activo) {
        setNombre(nombre);
        setIdFuncionalidad(idFuncionalidad);
        setIdTipoParametro(idTipoParametro);
        setActivo(activo);
    }

    public static ActualizarParametroDtoInput create(final String nombre, final UUID idFuncionalidad, final UUID idTipoParametro,
                                                     final boolean activo) {
        return new ActualizarParametroDtoInput(nombre, idFuncionalidad, idTipoParametro, activo);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public UUID getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(final UUID idFuncionalidad) {
        this.idFuncionalidad = UUIDHelper.getDefault(idFuncionalidad);
    }

    public UUID getIdTipoParametro() {
        return idTipoParametro;
    }

    public void setIdTipoParametro(final UUID idTipoParametro) {
        this.idTipoParametro = UUIDHelper.getDefault(idTipoParametro);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }
}
