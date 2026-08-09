package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearFuncionalidadDtoRequest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        validateNombre();
    }

    public String getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(final String idModulo) {
        this.idModulo = TextHelper.applyTrim(idModulo);
        validateIdModulo();
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(final String activo) {
        this.activo = activo == null ? "true" : TextHelper.applyTrim(activo).toLowerCase();
        validateActivo();
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(final String fechaInicio) {
        this.fechaInicio = TextHelper.applyTrim(fechaInicio);
        validateFechaInicio();
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(final String fechaFinal) {
        this.fechaFinal = TextHelper.applyTrim(fechaFinal);
        validateFechaFinal();
    }

    private void validateNombre() {
        if (TextHelper.isBlank(nombre)) {
            throw new FuncionalidadException("El nombre de la funcionalidad es obligatorio.");
        }
        if (nombre.length() < 3 || nombre.length() > 50) {
            throw new FuncionalidadException("El nombre debe tener entre 3 y 50 caracteres.");
        }
    }

    private void validateIdModulo() {
        if (TextHelper.isBlank(idModulo)) {
            throw new FuncionalidadException("El identificador del modulo es obligatorio.");
        }
        try {
            UUID.fromString(idModulo);
        } catch (IllegalArgumentException e) {
            throw new FuncionalidadException("El identificador del modulo no es valido. Valor recibido: " + idModulo);
        }
    }

    private void validateActivo() {
        if (!"true".equals(activo) && !"false".equals(activo)) {
            throw new FuncionalidadException("El estado activo debe ser 'true' o 'false'. Valor recibido: " + activo);
        }
    }

    private void validateFechaInicio() {
        if (!TextHelper.isBlank(fechaInicio)) {
            try {
                LocalDateTime.parse(fechaInicio, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new FuncionalidadException("La fecha de inicio no tiene un formato valido (yyyy-MM-dd HH:mm:ss). Valor recibido: " + fechaInicio);
            }
        }
    }

    private void validateFechaFinal() {
        if (!TextHelper.isBlank(fechaFinal)) {
            try {
                LocalDateTime.parse(fechaFinal, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new FuncionalidadException("La fecha final no tiene un formato valido (yyyy-MM-dd HH:mm:ss). Valor recibido: " + fechaFinal);
            }
        }
    }
}
