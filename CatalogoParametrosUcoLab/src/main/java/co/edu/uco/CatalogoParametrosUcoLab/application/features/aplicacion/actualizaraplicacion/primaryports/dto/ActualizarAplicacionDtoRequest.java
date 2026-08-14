package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarAplicacionDtoRequest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        validateNombre();
    }

    public String getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(final String idOrganizacion) {
        this.idOrganizacion = TextHelper.applyTrim(idOrganizacion);
        validateIdOrganizacion();
    }

    public String getActiva() {
        return activa;
    }

    public void setActiva(final String activa) {
        this.activa = activa == null ? "true" : TextHelper.applyTrim(activa).toLowerCase();
        validateActiva();
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
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-7"));
        }
        if (nombre.length() < 3 || nombre.length() > 50) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-6"));
        }
    }

    private void validateIdOrganizacion() {
        if (TextHelper.isBlank(idOrganizacion)) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-5"));
        }
        try {
            UUID.fromString(idOrganizacion);
        } catch (IllegalArgumentException e) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-4"));
        }
    }

    private void validateActiva() {
        if (!"true".equals(activa) && !"false".equals(activa)) {
            throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-3"));
        }
    }

    private void validateFechaInicio() {
        if (!TextHelper.isBlank(fechaInicio)) {
            try {
                LocalDateTime.parse(fechaInicio, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-2"));
            }
        }
    }

    private void validateFechaFinal() {
        if (!TextHelper.isBlank(fechaFinal)) {
            try {
                LocalDateTime.parse(fechaFinal, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw ValidationException.build(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper.getValue(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants.MESSAGE_PROPERTIES_FILE, "MSG-1"));
            }
        }
    }
}
