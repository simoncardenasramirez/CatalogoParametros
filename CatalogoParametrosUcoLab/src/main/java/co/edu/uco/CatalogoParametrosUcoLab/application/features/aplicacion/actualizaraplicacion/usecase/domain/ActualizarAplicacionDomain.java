package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;

public final class ActualizarAplicacionDomain extends Domain {

    private String nombre;
    private UUID idOrganizacion;
    private boolean activa;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    private ActualizarAplicacionDomain(final UUID id, final String nombre, final UUID idOrganizacion, final boolean activa,
                                      final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        super(id);
        setNombre(nombre);
        setIdOrganizacion(idOrganizacion);
        setActiva(activa);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static ActualizarAplicacionDomain create(final UUID id, final String nombre, final UUID idOrganizacion,
                                                   final boolean activa, final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        return new ActualizarAplicacionDomain(id, nombre, idOrganizacion, activa, fechaInicio, fechaFinal);
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

    public OffsetDateTime getFechaInicio() {
        return fechaInicio;
    }

    private void setFechaInicio(final OffsetDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public OffsetDateTime getFechaFinal() {
        return fechaFinal;
    }

    private void setFechaFinal(final OffsetDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
