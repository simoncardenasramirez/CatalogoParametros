package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class FuncionalidadEntity {

    private UUID id;
    private String nombre;
    private UUID idModulo;
    private boolean activo;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    private FuncionalidadEntity() {
        setId(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setIdModulo(UUIDHelper.getDefault());
        setActivo(true);
        setFechaInicio(null);
        setFechaFinal(null);
    }

    private FuncionalidadEntity(final UUID id, final String nombre, final UUID idModulo,
            final boolean activo, final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        setId(id);
        setNombre(nombre);
        setIdModulo(idModulo);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static FuncionalidadEntity create(final UUID id, final String nombre, final UUID idModulo,
            final boolean activo, final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        return new FuncionalidadEntity(id, nombre, idModulo, activo, fechaInicio, fechaFinal);
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

    public UUID getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(final UUID idModulo) {
        this.idModulo = UUIDHelper.getDefault(idModulo);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }

    public OffsetDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(final OffsetDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public OffsetDateTime getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(final OffsetDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}