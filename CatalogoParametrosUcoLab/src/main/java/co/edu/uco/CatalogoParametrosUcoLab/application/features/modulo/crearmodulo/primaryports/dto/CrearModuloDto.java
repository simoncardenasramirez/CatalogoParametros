package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto;

import java.util.UUID;

public final class CrearModuloDto {

    private String nombre;
    private UUID idAplicacion;
    private boolean activo;
    private String fechaInicio;
    private String fechaFinal;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public UUID getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(final UUID idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(final String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(final String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
