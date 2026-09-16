package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;

import java.time.OffsetDateTime;

public final class ActualizarOrganizacionDtoInput {

    private String nombre;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    public ActualizarOrganizacionDtoInput() {
        this("", null, null);
    }

    public ActualizarOrganizacionDtoInput(final String nombre) {
        this(nombre, null, null);
    }

    public ActualizarOrganizacionDtoInput(final String nombre, final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        setNombre(nombre); this.fechaInicio = fechaInicio; this.fechaFinal = fechaFinal;
    }

    public static ActualizarOrganizacionDtoInput create(final String nombre) {
        return new ActualizarOrganizacionDtoInput(nombre);
    }

    public static ActualizarOrganizacionDtoInput create(final String nombre, final OffsetDateTime fechaInicio,
            final OffsetDateTime fechaFinal) { return new ActualizarOrganizacionDtoInput(nombre, fechaInicio, fechaFinal); }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(final OffsetDateTime value) { fechaInicio = value; }
    public OffsetDateTime getFechaFinal() { return fechaFinal; }
    public void setFechaFinal(final OffsetDateTime value) { fechaFinal = value; }
}
