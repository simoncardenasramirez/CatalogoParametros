package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;

public final class CrearOrganizacionDomain extends Domain {

    private String nombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    private CrearOrganizacionDomain(final UUID id, final String nombre) {
        this(id, nombre, null, null);
    }

    private CrearOrganizacionDomain(final UUID id, final String nombre, final LocalDateTime fechaInicio,
            final LocalDateTime fechaFinal) {
        super(id);
        setNombre(nombre);
        this.fechaInicio = fechaInicio; this.fechaFinal = fechaFinal;
    }

    public static CrearOrganizacionDomain create(final UUID id, final String nombre) {
        return new CrearOrganizacionDomain(id, nombre);
    }

    public static CrearOrganizacionDomain create(final UUID id, final String nombre, final LocalDateTime fechaInicio,
            final LocalDateTime fechaFinal) { return new CrearOrganizacionDomain(id, nombre, fechaInicio, fechaFinal); }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFinal() { return fechaFinal; }
}
