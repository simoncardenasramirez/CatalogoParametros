package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearAplicacionDtoInput {

    private String nombre;
    private UUID idOrganizacion;
    private boolean activa;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    public CrearAplicacionDtoInput() {
        this(TextHelper.EMPTY, UUIDHelper.getDefault(), false, null, null);
    }

    public CrearAplicacionDtoInput(final String nombre, final UUID idOrganizacion, final boolean activa,
                                   final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        setNombre(nombre);
        setIdOrganizacion(idOrganizacion);
        setActiva(activa);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearAplicacionDtoInput create(final String nombre, final UUID idOrganizacion, final boolean activa,
                                                 final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        return new CrearAplicacionDtoInput(nombre, idOrganizacion, activa, fechaInicio, fechaFinal);
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

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(final LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(final LocalDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
