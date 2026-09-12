package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class ModuloEntity {

    private UUID id;
    private String nombre;
    private UUID idAplicacion;
    private boolean activo;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    public static ModuloEntity create(final UUID id, final String nombre, final UUID idAplicacion,
            final boolean activo, final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        var entity = new ModuloEntity();
        entity.setId(id);
        entity.setNombre(nombre);
        entity.setIdAplicacion(idAplicacion);
        entity.setActivo(activo);
        entity.setFechaInicio(fechaInicio);
        entity.setFechaFinal(fechaFinal);
        return entity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
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
        this.idAplicacion = idAplicacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
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
