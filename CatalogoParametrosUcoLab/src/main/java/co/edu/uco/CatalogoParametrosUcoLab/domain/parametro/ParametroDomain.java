package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.Domain;

public final class ParametroDomain extends Domain {

    private String nombre;
    private UUID idFuncionalidad;
    private UUID idTipoParametro;
    private boolean activo;

    private ParametroDomain(final UUID id, final String nombre, final UUID idFuncionalidad, final UUID idTipoParametro,
            final boolean activo) {
        super(id);
        setNombre(nombre);
        setIdFuncionalidad(idFuncionalidad);
        setIdTipoParametro(idTipoParametro);
        setActivo(activo);
    }

    public static ParametroDomain create(final UUID id, final String nombre, final UUID idFuncionalidad,
            final UUID idTipoParametro, final boolean activo) {
        return new ParametroDomain(id, nombre, idFuncionalidad, idTipoParametro, activo);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public UUID getIdFuncionalidad() {
        return idFuncionalidad;
    }

    private void setIdFuncionalidad(final UUID idFuncionalidad) {
        this.idFuncionalidad = UUIDHelper.getDefault(idFuncionalidad);
    }

    public UUID getIdTipoParametro() {
        return idTipoParametro;
    }

    private void setIdTipoParametro(final UUID idTipoParametro) {
        this.idTipoParametro = UUIDHelper.getDefault(idTipoParametro);
    }

    public boolean isActivo() {
        return activo;
    }

    private void setActivo(final boolean activo) {
        this.activo = activo;
    }
}
