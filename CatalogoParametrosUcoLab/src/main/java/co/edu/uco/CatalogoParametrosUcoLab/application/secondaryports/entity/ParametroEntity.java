package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class ParametroEntity {

    private UUID id;
    private String nombre;
    private String valor;
    private String descripcion;
    private boolean activo;

    private ParametroEntity() {
        setId(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setValor(TextHelper.EMPTY);
        setDescripcion(TextHelper.EMPTY);
        setActivo(true);
    }

    private ParametroEntity(final UUID id, final String nombre, final String valor, final String descripcion,
            final boolean activo) {
        setId(id);
        setNombre(nombre);
        setValor(valor);
        setDescripcion(descripcion);
        setActivo(activo);
    }

    public static ParametroEntity create(final UUID id, final String nombre, final String valor,
            final String descripcion, final boolean activo) {
        return new ParametroEntity(id, nombre, valor, descripcion, activo);
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
