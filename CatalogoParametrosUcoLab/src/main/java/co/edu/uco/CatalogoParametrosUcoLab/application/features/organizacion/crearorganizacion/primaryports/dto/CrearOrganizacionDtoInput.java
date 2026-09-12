package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto;

import java.time.OffsetDateTime;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearOrganizacionDtoInput {

    private String nombre;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    public CrearOrganizacionDtoInput() {
        this(TextHelper.EMPTY, null, null);
    }

    public CrearOrganizacionDtoInput(final String nombre) {
        this(nombre, null, null);
    }

    public CrearOrganizacionDtoInput(final String nombre, final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        setNombre(nombre); this.fechaInicio = fechaInicio; this.fechaFinal = fechaFinal;
    }

    public static CrearOrganizacionDtoInput create(final String nombre) {
        return new CrearOrganizacionDtoInput(nombre);
    }

    public static CrearOrganizacionDtoInput create(final String nombre, final OffsetDateTime fechaInicio,
            final OffsetDateTime fechaFinal) { return new CrearOrganizacionDtoInput(nombre, fechaInicio, fechaFinal); }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(final OffsetDateTime value) { fechaInicio = value; }
    public OffsetDateTime getFechaFinal() { return fechaFinal; }
    public void setFechaFinal(final OffsetDateTime value) { fechaFinal = value; }
}
