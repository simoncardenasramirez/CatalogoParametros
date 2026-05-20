package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.funcionalidad;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearFuncionalidadDto {

    private String nombre;
    private UUID idModulo;
    private boolean activo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    public CrearFuncionalidadDto() {
        setNombre(TextHelper.EMPTY);
        setIdModulo(UUIDHelper.getDefault());
        setActivo(true);
        setFechaInicio(null);
        setFechaFinal(null);
    }

    public CrearFuncionalidadDto(final String nombre, final UUID idModulo, final boolean activo,
            final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        setNombre(nombre);
        setIdModulo(idModulo);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearFuncionalidadDto create(final String nombre, final UUID idModulo, final boolean activo,
            final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        return new CrearFuncionalidadDto(nombre, idModulo, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public UUID getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(final UUID idModulo) {
        this.idModulo = UUIDHelper.getDefault(idModulo);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
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