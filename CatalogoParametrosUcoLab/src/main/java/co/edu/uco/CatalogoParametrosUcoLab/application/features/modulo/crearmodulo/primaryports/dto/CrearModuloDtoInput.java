package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearModuloDtoInput {

    private String nombre;
    private UUID idAplicacion;
    private boolean activo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    public CrearModuloDtoInput() {
        this("", UUIDHelper.getDefault(), false, null, null);
    }

    public CrearModuloDtoInput(final String nombre, final UUID idAplicacion, final boolean activo,
                               final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        setNombre(nombre);
        setIdAplicacion(idAplicacion);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearModuloDtoInput create(final String nombre, final UUID idAplicacion, final boolean activo,
                                             final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        return new CrearModuloDtoInput(nombre, idAplicacion, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
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

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(final LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(final LocalDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
