package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;


import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

public final class ActualizarOrganizacionDtoRequest {

    private String nombre;
    private String fechaInicio;
    private String fechaFinal;

    public ActualizarOrganizacionDtoRequest() {
        this.nombre = TextHelper.EMPTY;
        this.fechaInicio = TextHelper.EMPTY;
        this.fechaFinal = TextHelper.EMPTY;
    }

    public ActualizarOrganizacionDtoRequest(final String nombre) {
        this(nombre, TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public ActualizarOrganizacionDtoRequest(final String nombre, final String fechaInicio, final String fechaFinal) {
        setNombre(nombre); setFechaInicio(fechaInicio); setFechaFinal(fechaFinal);
    }

    public static ActualizarOrganizacionDtoRequest create(final String nombre) {
        return new ActualizarOrganizacionDtoRequest(nombre);
    }

    public static ActualizarOrganizacionDtoRequest create(final String nombre, final String fechaInicio, final String fechaFinal) {
        return new ActualizarOrganizacionDtoRequest(nombre, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
        ValidateHelper.validateNombre(this.nombre, "de la organizacion");
    }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(final String value) { fechaInicio = TextHelper.applyTrim(value); ValidateHelper.validateFecha(fechaInicio, "fecha de inicio"); }
    public String getFechaFinal() { return fechaFinal; }
    public void setFechaFinal(final String value) { fechaFinal = TextHelper.applyTrim(value); ValidateHelper.validateFecha(fechaFinal, "fecha final"); }
}
