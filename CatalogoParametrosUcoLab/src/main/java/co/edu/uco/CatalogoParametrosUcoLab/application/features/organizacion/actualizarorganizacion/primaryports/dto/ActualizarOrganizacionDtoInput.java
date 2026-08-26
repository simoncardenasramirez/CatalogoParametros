package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;

import java.time.LocalDateTime;

public final class ActualizarOrganizacionDtoInput {

    private String nombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    public ActualizarOrganizacionDtoInput() {
        this("", null, null);
    }

    public ActualizarOrganizacionDtoInput(final String nombre) {
        this(nombre, null, null);
    }

    public ActualizarOrganizacionDtoInput(final String nombre, final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        setNombre(nombre); this.fechaInicio = fechaInicio; this.fechaFinal = fechaFinal;
    }

    public static ActualizarOrganizacionDtoInput create(final String nombre) {
        return new ActualizarOrganizacionDtoInput(nombre);
    }

    public static ActualizarOrganizacionDtoInput create(final String nombre, final LocalDateTime fechaInicio,
            final LocalDateTime fechaFinal) { return new ActualizarOrganizacionDtoInput(nombre, fechaInicio, fechaFinal); }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(final LocalDateTime value) { fechaInicio = value; }
    public LocalDateTime getFechaFinal() { return fechaFinal; }
    public void setFechaFinal(final LocalDateTime value) { fechaFinal = value; }
}
