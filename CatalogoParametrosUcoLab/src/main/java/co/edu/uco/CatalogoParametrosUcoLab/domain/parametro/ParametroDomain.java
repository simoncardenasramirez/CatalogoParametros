package co.edu.uco.CatalogoParametrosUcoLab.domain.parametro;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.Domain;

public final class ParametroDomain extends Domain {

    private String nombre;
    private String valor;
    private String descripcion;
    private boolean activo;

    private ParametroDomain(final UUID id, final String nombre, final String valor, final String descripcion,
            final boolean activo) {
        super(id);
        setNombre(nombre);
        setValor(valor);
        setDescripcion(descripcion);
        setActivo(activo);
    }

    public static ParametroDomain create(final UUID id, final String nombre, final String valor,
            final String descripcion, final boolean activo) {
        return new ParametroDomain(id, nombre, valor, descripcion, activo);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public String getValor() {
        return valor;
    }

    private void setValor(final String valor) {
        this.valor = TextHelper.applyTrim(valor);
    }

    public String getDescripcion() {
        return descripcion;
    }

    private void setDescripcion(final String descripcion) {
        this.descripcion = TextHelper.applyTrim(descripcion);
    }

    public boolean isActivo() {
        return activo;
    }

    private void setActivo(final boolean activo) {
        this.activo = activo;
    }
}
