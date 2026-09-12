package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;

public final class CrearModuloDomain extends Domain {

    private String nombre;
    private UUID idAplicacion;
    private boolean activo;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFinal;

    private CrearModuloDomain(final UUID id, final String nombre, final UUID idAplicacion, final boolean activo,
                              final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        super(id);
        setNombre(nombre);
        setIdAplicacion(idAplicacion);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearModuloDomain create(final UUID id, final String nombre, final UUID idAplicacion,
                                           final boolean activo, final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        return new CrearModuloDomain(id, nombre, idAplicacion, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public UUID getIdAplicacion() {
        return idAplicacion;
    }

    private void setIdAplicacion(final UUID idAplicacion) {
        this.idAplicacion = UUIDHelper.getDefault(idAplicacion);
    }

    public boolean isActivo() {
        return activo;
    }

    private void setActivo(final boolean activo) {
        this.activo = activo;
    }

    public OffsetDateTime getFechaInicio() {
        return fechaInicio;
    }

    private void setFechaInicio(final OffsetDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public OffsetDateTime getFechaFinal() {
        return fechaFinal;
    }

    private void setFechaFinal(final OffsetDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
