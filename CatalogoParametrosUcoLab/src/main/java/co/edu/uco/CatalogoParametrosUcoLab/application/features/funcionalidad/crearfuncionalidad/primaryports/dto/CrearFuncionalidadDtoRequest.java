package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto;


import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

public final class CrearFuncionalidadDtoRequest {

    private String nombre;
    private String idModulo;
    private String activo;
    private String fechaInicio;
    private String fechaFinal;

    public CrearFuncionalidadDtoRequest() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, "true", TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public CrearFuncionalidadDtoRequest(final String nombre, final String idModulo, final String activo,
                                 final String fechaInicio, final String fechaFinal) {
        setNombre(nombre);
        setIdModulo(idModulo);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearFuncionalidadDtoRequest create(final String nombre, final String idModulo, final String activo,
                                               final String fechaInicio, final String fechaFinal) {
        return new CrearFuncionalidadDtoRequest(nombre, idModulo, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
        ValidateHelper.validateNombre(this.nombre, "de la funcionalidad");
    }

    public String getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(final String idModulo) {
        this.idModulo = TextHelper.applyTrim(idModulo);
        ValidateHelper.validateId(this.idModulo, "identificador del modulo");
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
