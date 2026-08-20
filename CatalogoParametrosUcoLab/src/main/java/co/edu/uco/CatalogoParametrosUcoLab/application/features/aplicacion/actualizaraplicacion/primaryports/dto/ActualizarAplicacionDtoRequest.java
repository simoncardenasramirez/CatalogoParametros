package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto;


import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

public final class ActualizarAplicacionDtoRequest {

    private String nombre;
    private String idOrganizacion;
    private String activa;
    private String fechaInicio;
    private String fechaFinal;

    public ActualizarAplicacionDtoRequest() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, "true", TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public ActualizarAplicacionDtoRequest(final String nombre, final String idOrganizacion, final String activa,
                                      final String fechaInicio, final String fechaFinal) {
        setNombre(nombre);
        setIdOrganizacion(idOrganizacion);
        setActiva(activa);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static ActualizarAplicacionDtoRequest create(final String nombre, final String idOrganizacion, final String activa,
                                                    final String fechaInicio, final String fechaFinal) {
        return new ActualizarAplicacionDtoRequest(nombre, idOrganizacion, activa, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
        ValidateHelper.validateNombre(this.nombre, "de la aplicacion");
    }

    public String getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(final String idOrganizacion) {
        this.idOrganizacion = TextHelper.applyTrim(idOrganizacion);
        ValidateHelper.validateId(this.idOrganizacion, "identificador de la organizacion");
    }

    public String getActiva() {
        return activa;
    }

    public void setActiva(final String activa) {
        this.activa = activa == null ? "true" : TextHelper.applyTrim(activa).toLowerCase();
        ValidateHelper.validateActivo(this.activa);
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
