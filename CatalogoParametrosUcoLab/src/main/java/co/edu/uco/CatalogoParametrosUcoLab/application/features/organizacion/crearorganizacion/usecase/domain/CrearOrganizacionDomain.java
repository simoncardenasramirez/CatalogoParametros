package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;

public final class CrearOrganizacionDomain extends Domain {

    private String nombre;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    private CrearOrganizacionDomain(final UUID id, final String nombre) {
        this(id, nombre, null, null);
    }

    private CrearOrganizacionDomain(final UUID id, final String nombre, final OffsetDateTime fechaInicio,
            final OffsetDateTime fechaFinal) {
        super(id);
        setNombre(nombre);
        this.fechaInicio = fechaInicio; this.fechaFinal = fechaFinal;
    }

    public static CrearOrganizacionDomain create(final UUID id, final String nombre) {
        return new CrearOrganizacionDomain(id, nombre);
    }

    public static CrearOrganizacionDomain create(final UUID id, final String nombre, final OffsetDateTime fechaInicio,
            final OffsetDateTime fechaFinal) { return new CrearOrganizacionDomain(id, nombre, fechaInicio, fechaFinal); }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public OffsetDateTime getFechaFinal() { return fechaFinal; }
}
