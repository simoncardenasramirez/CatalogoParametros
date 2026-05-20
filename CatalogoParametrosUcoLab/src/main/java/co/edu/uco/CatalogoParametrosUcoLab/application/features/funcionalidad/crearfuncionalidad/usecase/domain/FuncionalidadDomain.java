package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;

public final class FuncionalidadDomain extends Domain {

    private String nombre;
    private UUID idModulo;
    private boolean activo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;

    private FuncionalidadDomain(final UUID id, final String nombre, final UUID idModulo, final boolean activo,
            final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        super(id);
        setNombre(nombre);
        setIdModulo(idModulo);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static FuncionalidadDomain create(final UUID id, final String nombre, final UUID idModulo,
            final boolean activo, final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        return new FuncionalidadDomain(id, nombre, idModulo, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public UUID getIdModulo() {
        return idModulo;
    }

    private void setIdModulo(final UUID idModulo) {
        this.idModulo = UUIDHelper.getDefault(idModulo);
    }

    public boolean isActivo() {
        return activo;
    }

    private void setActivo(final boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    private void setFechaInicio(final LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFinal() {
        return fechaFinal;
    }

    private void setFechaFinal(final LocalDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
