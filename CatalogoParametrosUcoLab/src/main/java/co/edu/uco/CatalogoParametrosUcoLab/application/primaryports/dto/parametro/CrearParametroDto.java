package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.parametro;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearParametroDto {

    private String nombre;
    private String valor;
    private String descripcion;
    private boolean activo;

    public CrearParametroDto() {
        setNombre(TextHelper.EMPTY);
        setValor(TextHelper.EMPTY);
        setDescripcion(TextHelper.EMPTY);
        setActivo(true);
    }

    public CrearParametroDto(final String nombre, final String valor, final String descripcion, final boolean activo) {
        setNombre(nombre);
        setValor(valor);
        setDescripcion(descripcion);
        setActivo(activo);
    }

    public static CrearParametroDto create(final String nombre, final String valor, final String descripcion,
            final boolean activo) {
        return new CrearParametroDto(nombre, valor, descripcion, activo);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public String getValor() {
        return valor;
    }

    public void setValor(final String valor) {
        this.valor = TextHelper.applyTrim(valor);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = TextHelper.applyTrim(descripcion);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }
}
