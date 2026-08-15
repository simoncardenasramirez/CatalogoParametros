package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearModuloDtoRequest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String nombre;
    private String idAplicacion;
    private String activo;
    private String fechaInicio;
    private String fechaFinal;

    public CrearModuloDtoRequest() {
        this(TextHelper.EMPTY, TextHelper.EMPTY, "true", TextHelper.EMPTY, TextHelper.EMPTY);
    }

    public CrearModuloDtoRequest(final String nombre, final String idAplicacion, final String activo,
                          final String fechaInicio, final String fechaFinal) {
        setNombre(nombre);
        setIdAplicacion(idAplicacion);
        setActivo(activo);
        setFechaInicio(fechaInicio);
        setFechaFinal(fechaFinal);
    }

    public static CrearModuloDtoRequest create(final String nombre, final String idAplicacion, final String activo,
                                        final String fechaInicio, final String fechaFinal) {
        return new CrearModuloDtoRequest(nombre, idAplicacion, activo, fechaInicio, fechaFinal);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
        validateNombre();
    }

    public String getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(final String idAplicacion) {
        this.idAplicacion = TextHelper.applyTrim(idAplicacion);
        validateIdAplicacion();
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
            throw ValidationException.build("El nombre del modulo es obligatorio.");
        }
        if (nombre.length() < 3 || nombre.length() > 50) {
            throw ValidationException.build("El nombre debe tener entre 3 y 50 caracteres.");
        }
    }

    private void validateIdAplicacion() {
        if (TextHelper.isBlank(idAplicacion)) {
            throw ValidationException.build("El identificador de la aplicacion es obligatorio.");
        }
        try {
            UUID.fromString(idAplicacion);
        } catch (IllegalArgumentException e) {
            throw ValidationException.build("El identificador de la aplicacion no es valido. Valor recibido: " + idAplicacion);
        }
    }

    private void validateActivo() {
        if (!"true".equals(activo) && !"false".equals(activo)) {
            throw ValidationException.build("El estado activo debe ser 'true' o 'false'. Valor recibido: " + activo);
        }
    }

    private void validateFechaInicio() {
        if (!TextHelper.isBlank(fechaInicio)) {
            try {
                LocalDateTime.parse(fechaInicio, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw ValidationException.build("La fecha de inicio no tiene un formato valido (yyyy-MM-dd HH:mm:ss). Valor recibido: " + fechaInicio);
            }
        }
    }

    private void validateFechaFinal() {
        if (!TextHelper.isBlank(fechaFinal)) {
            try {
                LocalDateTime.parse(fechaFinal, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw ValidationException.build("La fecha final no tiene un formato valido (yyyy-MM-dd HH:mm:ss). Valor recibido: " + fechaFinal);
            }
        }
    }
}
