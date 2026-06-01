package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;

public final class CrearAplicacionDomain extends Domain {

    private String nombre;
    private UUID idOrganizacion;
    private boolean activa;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    private CrearAplicacionDomain(final UUID id, final String nombre, final UUID idOrganizacion, final boolean activa,
                                  final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        super(id);
        setNombre(nombre);
        setIdOrganizacion(idOrganizacion);
        setActiva(activa);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearAplicacionDomain create(final UUID id, final String nombre, final UUID idOrganizacion,
                                               final boolean activa, final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        return new CrearAplicacionDomain(id, nombre, idOrganizacion, activa, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public UUID getIdOrganizacion() {
        return idOrganizacion;
    }

    private void setIdOrganizacion(final UUID idOrganizacion) {
        this.idOrganizacion = UUIDHelper.getDefault(idOrganizacion);
    }

    public boolean isActiva() {
        return activa;
    }

    private void setActiva(final boolean activa) {
        this.activa = activa;
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
