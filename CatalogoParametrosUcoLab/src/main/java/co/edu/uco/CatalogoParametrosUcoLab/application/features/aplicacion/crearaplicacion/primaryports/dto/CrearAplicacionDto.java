package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearAplicacionDto {

    private String nombre;
    private String idOrganizacion;
    private String activa;
    private String fechaInicio;
    private String fechaFinal;

    public CrearAplicacionDto() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, "true", TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public CrearAplicacionDto(final String nombre, final String idOrganizacion, final String activa,
                              final String fechaInicio, final String fechaFinal) {
        setNombre(nombre);
        setIdOrganizacion(idOrganizacion);
        setActiva(activa);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearAplicacionDto create(final String nombre, final String idOrganizacion, final String activa,
                                            final String fechaInicio, final String fechaFinal) {
        return new CrearAplicacionDto(nombre, idOrganizacion, activa, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public String getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(final String idOrganizacion) {
        this.idOrganizacion = TextHelper.applyTrim(idOrganizacion);
    }

    public String getActiva() {
        return activa;
    }

    public void setActiva(final String activa) {
        this.activa = activa == null ? "true" : TextHelper.applyTrim(activa).toLowerCase();
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(final String fechaInicio) {
        this.fechaInicio = TextHelper.applyTrim(fechaInicio);
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(final String fechaFinal) {
        this.fechaFinal = TextHelper.applyTrim(fechaFinal);
    }
}
