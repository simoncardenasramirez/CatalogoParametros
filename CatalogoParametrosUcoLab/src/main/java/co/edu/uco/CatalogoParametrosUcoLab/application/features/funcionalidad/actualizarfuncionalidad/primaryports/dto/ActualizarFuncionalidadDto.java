package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarFuncionalidadDto {

    private String nombre;
    private String idModulo;
    private String activo;
    private String fechaInicio;
    private String fechaFinal;

    public ActualizarFuncionalidadDto() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, "true", TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public ActualizarFuncionalidadDto(final String nombre, final String idModulo, final String activo,
                                      final String fechaInicio, final String fechaFinal) {
        setNombre(nombre);
        setIdModulo(idModulo);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static ActualizarFuncionalidadDto create(final String nombre, final String idModulo, final String activo,
                                                    final String fechaInicio, final String fechaFinal) {
        return new ActualizarFuncionalidadDto(nombre, idModulo, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public String getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(final String idModulo) {
        this.idModulo = TextHelper.applyTrim(idModulo);
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
