package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto;


import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

public final class ActualizarModuloDtoRequest {

    private String nombre;
    private String idAplicacion;
    private String activo;
    private String fechaInicio;
    private String fechaFinal;

    public ActualizarModuloDtoRequest() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, "true", TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public ActualizarModuloDtoRequest(final String nombre, final String idAplicacion, final String activo,
                                      final String fechaInicio, final String fechaFinal) {
        setNombre(nombre);
        setIdAplicacion(idAplicacion);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static ActualizarModuloDtoRequest create(final String nombre, final String idAplicacion, final String activo,
                                                    final String fechaInicio, final String fechaFinal) {
        return new ActualizarModuloDtoRequest(nombre, idAplicacion, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
        ValidateHelper.validateNombre(this.nombre, "del modulo");
    }

    public String getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(final String idAplicacion) {
        this.idAplicacion = TextHelper.applyTrim(idAplicacion);
        ValidateHelper.validateId(this.idAplicacion, "identificador de la aplicacion");
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(final String activo) {
        this.activo = activo == null ? "true" : TextHelper.applyTrim(activo).toLowerCase();
        ValidateHelper.validateActivo(this.activo);
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(final String fechaInicio) {
        this.fechaInicio = TextHelper.applyTrim(fechaInicio);
        ValidateHelper.validateFecha(this.fechaInicio, "fecha de inicio");
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(final String fechaFinal) {
        this.fechaFinal = TextHelper.applyTrim(fechaFinal);
        ValidateHelper.validateFecha(this.fechaFinal, "fecha final");
    }
}
