package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class ActualizarModuloDtoInput {

    private String nombre;
    private UUID idAplicacion;
    private boolean activo;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    public ActualizarModuloDtoInput() {
        this(TextHelper.EMPTY, UUIDHelper.getDefault(), false, null, null);
    }

    public ActualizarModuloDtoInput(final String nombre, final UUID idAplicacion, final boolean activo,
                                   final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        setNombre(nombre);
        setIdAplicacion(idAplicacion);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static ActualizarModuloDtoInput create(final String nombre, final UUID idAplicacion, final boolean activo,
                                                 final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        return new ActualizarModuloDtoInput(nombre, idAplicacion, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public UUID getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(final UUID idAplicacion) {
        this.idAplicacion = UUIDHelper.getDefault(idAplicacion);
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
