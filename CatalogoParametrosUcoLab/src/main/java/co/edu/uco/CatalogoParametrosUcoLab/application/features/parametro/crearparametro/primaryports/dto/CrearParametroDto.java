package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

import java.util.UUID;

public final class CrearParametroDto {

    private String nombre;
    private UUID idFuncionalidad;
    private UUID idTipoParametro;
    private boolean activo;

    public CrearParametroDto() {
        setNombre(TextHelper.EMPTY);
        setIdFuncionalidad(UUIDHelper.getDefault());
        setIdTipoParametro(UUIDHelper.getDefault());
        setActivo(true);
    }

    public CrearParametroDto(final String nombre, final UUID idFuncionalidad, final UUID idTipoParametro,
            final boolean activo) {
        setNombre(nombre);
        setIdFuncionalidad(idFuncionalidad);
        setIdTipoParametro(idTipoParametro);
        setActivo(activo);
    }

    public static CrearParametroDto create(final String nombre, final UUID idFuncionalidad, final UUID idTipoParametro,
            final boolean activo) {
        return new CrearParametroDto(nombre, idFuncionalidad, idTipoParametro, activo);
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
