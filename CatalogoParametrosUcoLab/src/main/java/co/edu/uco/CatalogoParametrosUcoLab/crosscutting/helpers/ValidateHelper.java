package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

public final class ValidateHelper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ValidateHelper() {
    }

    public static void validateNombre(final String nombre, final String entityDescription) {
        if (TextHelper.isBlank(nombre)) {
            throw ValidationException.build("El nombre " + entityDescription + " es obligatorio.");
        }
        if (nombre.length() < 3 || nombre.length() > 50) {
            throw ValidationException.build("El nombre debe tener entre 3 y 50 caracteres.");
        }
    }

    public static void validateId(final String id, final String fieldDescription) {
        if (TextHelper.isBlank(id)) {
            throw ValidationException.build("El " + fieldDescription + " es obligatorio.");
        }
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw ValidationException.build("El " + fieldDescription + " no es valido. Valor recibido: " + id);
        }
    }

    public static void validateActivo(final String activo) {
        final var normalized = TextHelper.isBlank(activo) ? TextHelper.EMPTY : activo.trim().toLowerCase();
        if (!"true".equals(normalized) && !"false".equals(normalized)) {
            throw ValidationException.build("El estado activo debe ser 'true' o 'false'. Valor recibido: " + activo);
        }
    }

    public static void validateFecha(final String fecha, final String fieldName) {
        if (!TextHelper.isBlank(fecha)) {
            try {
                LocalDateTime.parse(fecha, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw ValidationException.build("La " + fieldName + " no tiene un formato valido (yyyy-MM-dd HH:mm:ss). Valor recibido: " + fecha);
            }
        }
    }
}
