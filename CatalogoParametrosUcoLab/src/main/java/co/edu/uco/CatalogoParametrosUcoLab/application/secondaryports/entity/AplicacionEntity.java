package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class AplicacionEntity {

    private UUID id;
    private String nombre;
    private UUID idOrganizacion;
    private boolean activa;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    private AplicacionEntity() {
        setId(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setIdOrganizacion(UUIDHelper.getDefault());
        setActiva(true);
        setFechaInicio(null);
        setFechaFinal(null);
    }

    private AplicacionEntity(final UUID id, final String nombre, final UUID idOrganizacion, final boolean activa,
            final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        setId(id);
        setNombre(nombre);
        setIdOrganizacion(idOrganizacion);
        setActiva(activa);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static AplicacionEntity create(final UUID id, final String nombre, final UUID idOrganizacion,
            final boolean activa, final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        return new AplicacionEntity(id, nombre, idOrganizacion, activa, fechaInicio, fechaFinal);
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

    public UUID getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(final UUID idOrganizacion) {
        this.idOrganizacion = UUIDHelper.getDefault(idOrganizacion);
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(final boolean activa) {
        this.activa = activa;
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
