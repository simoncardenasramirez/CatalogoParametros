package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearModuloDto {

    private String nombre;
    private String idAplicacion;
    private String activo;
    private String fechaInicio;
    private String fechaFinal;

    public CrearModuloDto() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, "true", TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public CrearModuloDto(final String nombre, final String idAplicacion, final String activo,
                          final String fechaInicio, final String fechaFinal) {
        setNombre(nombre);
        setIdAplicacion(idAplicacion);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearModuloDto create(final String nombre, final String idAplicacion, final String activo,
                                        final String fechaInicio, final String fechaFinal) {
        return new CrearModuloDto(nombre, idAplicacion, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public String getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(final String idAplicacion) {
        this.idAplicacion = TextHelper.applyTrim(idAplicacion);
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(final String activo) {
        this.activo = activo == null ? "true" : TextHelper.applyTrim(activo).toLowerCase();
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
