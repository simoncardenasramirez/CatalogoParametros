package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class ActualizarFuncionalidadDtoInput {

    private String nombre;
    private UUID idModulo;
    private boolean activo;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    public ActualizarFuncionalidadDtoInput() {
        this(TextHelper.EMPTY, UUIDHelper.getDefault(), false, null, null);
    }

    public ActualizarFuncionalidadDtoInput(final String nombre, final UUID idModulo, final boolean activo,
                                           final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        setNombre(nombre);
        setIdModulo(idModulo);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static ActualizarFuncionalidadDtoInput create(final String nombre, final UUID idModulo, final boolean activo,
                                                         final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        return new ActualizarFuncionalidadDtoInput(nombre, idModulo, activo, fechaInicio, fechaFinal);
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
