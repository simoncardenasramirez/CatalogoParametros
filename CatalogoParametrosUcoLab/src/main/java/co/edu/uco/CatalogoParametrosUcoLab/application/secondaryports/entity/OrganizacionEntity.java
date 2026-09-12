package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class OrganizacionEntity {

    private UUID id;
    private String nombre;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    private OrganizacionEntity() {
        setId(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setFechaInicio(null);
        setFechaFinal(null);
    }

    private OrganizacionEntity(final UUID id, final String nombre) {
        this(id, nombre, null, null);
    }

    private OrganizacionEntity(final UUID id, final String nombre, final OffsetDateTime fechaInicio,
            final OffsetDateTime fechaFinal) {
        setId(id);
        setNombre(nombre);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static OrganizacionEntity create(final UUID id, final String nombre) {
        return new OrganizacionEntity(id, nombre);
    }

    public static OrganizacionEntity create(final UUID id, final String nombre, final OffsetDateTime fechaInicio,
            final OffsetDateTime fechaFinal) {
        return new OrganizacionEntity(id, nombre, fechaInicio, fechaFinal);
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

    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(final OffsetDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public OffsetDateTime getFechaFinal() { return fechaFinal; }
    public void setFechaFinal(final OffsetDateTime fechaFinal) { this.fechaFinal = fechaFinal; }
}
