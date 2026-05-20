package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class ParametroEntity {

    private UUID id;
    private String nombre;
    private UUID idFuncionalidad;
    private UUID idTipoParametro;
    private boolean activo;

    private ParametroEntity() {
        setId(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setIdFuncionalidad(UUIDHelper.getDefault());
        setIdTipoParametro(UUIDHelper.getDefault());
        setActivo(true);
    }

    private ParametroEntity(final UUID id, final String nombre, final UUID idFuncionalidad, final UUID idTipoParametro,
            final boolean activo) {
        setId(id);
        setNombre(nombre);
        setIdFuncionalidad(idFuncionalidad);
        setIdTipoParametro(idTipoParametro);
        setActivo(activo);
    }

    public static ParametroEntity create(final UUID id, final String nombre, final UUID idFuncionalidad,
            final UUID idTipoParametro, final boolean activo) {
        return new ParametroEntity(id, nombre, idFuncionalidad, idTipoParametro, activo);
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = UUIDHelper.getDefault(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
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
