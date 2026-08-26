package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class OrganizacionEntity {

    private UUID id;
    private String nombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    private OrganizacionEntity() {
        setId(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setFechaInicio(null);
        setFechaFinal(null);
    }

    private OrganizacionEntity(final UUID id, final String nombre) {
        this(id, nombre, null, null);
    }

    private OrganizacionEntity(final UUID id, final String nombre, final LocalDateTime fechaInicio,
            final LocalDateTime fechaFinal) {
        setId(id);
        setNombre(nombre);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static OrganizacionEntity create(final UUID id, final String nombre) {
        return new OrganizacionEntity(id, nombre);
    }

    public static OrganizacionEntity create(final UUID id, final String nombre, final LocalDateTime fechaInicio,
            final LocalDateTime fechaFinal) {
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

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(final LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFinal() { return fechaFinal; }
    public void setFechaFinal(final LocalDateTime fechaFinal) { this.fechaFinal = fechaFinal; }
}
