package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;

public final class CrearOrganizacionDomain extends Domain {

    private String nombre;
    private boolean activo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    private CrearOrganizacionDomain(final UUID id, final String nombre, final boolean activo,
                                    final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        super(id);
        setNombre(nombre);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearOrganizacionDomain create(final UUID id, final String nombre, final boolean activo,
                                                 final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        return new CrearOrganizacionDomain(id, nombre, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public boolean isActivo() {
        return activo;
    }

    private void setActivo(final boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    private void setFechaInicio(final LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFinal() {
        return fechaFinal;
    }

    private void setFechaFinal(final LocalDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
