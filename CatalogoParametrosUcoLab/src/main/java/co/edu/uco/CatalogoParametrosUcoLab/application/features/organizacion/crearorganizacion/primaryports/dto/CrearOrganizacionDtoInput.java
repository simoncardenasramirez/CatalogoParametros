package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto;

import java.time.LocalDateTime;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearOrganizacionDtoInput {

    private String nombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    public CrearOrganizacionDtoInput() {
        this(TextHelper.EMPTY, null, null);
    }

    public CrearOrganizacionDtoInput(final String nombre) {
        this(nombre, null, null);
    }

    public CrearOrganizacionDtoInput(final String nombre, final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        setNombre(nombre); this.fechaInicio = fechaInicio; this.fechaFinal = fechaFinal;
    }

    public static CrearOrganizacionDtoInput create(final String nombre) {
        return new CrearOrganizacionDtoInput(nombre);
    }

    public static CrearOrganizacionDtoInput create(final String nombre, final LocalDateTime fechaInicio,
            final LocalDateTime fechaFinal) { return new CrearOrganizacionDtoInput(nombre, fechaInicio, fechaFinal); }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(final LocalDateTime value) { fechaInicio = value; }
    public LocalDateTime getFechaFinal() { return fechaFinal; }
    public void setFechaFinal(final LocalDateTime value) { fechaFinal = value; }
}
