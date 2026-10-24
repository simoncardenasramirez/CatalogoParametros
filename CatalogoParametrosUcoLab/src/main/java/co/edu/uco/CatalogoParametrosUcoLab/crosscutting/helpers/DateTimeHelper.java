package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import tools.jackson.databind.JsonNode;

public final class DateTimeHelper {

    private DateTimeHelper() {
        super();
    }

    public static OffsetDateTime parse(final JsonNode dateNode) {
        if (dateNode == null || dateNode.isNull() || TextHelper.isBlank(dateNode.asString())) {
            return null;
        }
        var text = dateNode.asString();
        if (text.startsWith("d'") && text.endsWith("'")) {
            text = text.substring(2, text.length() - 1);
        }
        if (text.length() == 16) {
            text += ":00";
        }
        try {
            return toUtc(OffsetDateTime.parse(text));
        } catch (final DateTimeParseException exception) {
            // Compatibilidad de lectura: los tokens historicos sin zona se guardaban en UTC.
            try { return LocalDateTime.parse(text).atOffset(ZoneOffset.UTC); }
            catch (final DateTimeParseException invalid) { return null; }
        }
    }

    public static String format(final OffsetDateTime dateTime) {
        return dateTime == null ? "null" : "'" + java.time.format.DateTimeFormatter.ISO_INSTANT.format(dateTime.toInstant()) + "'";
    }
    public static OffsetDateTime toUtc(final OffsetDateTime fecha) {
        return fecha == null ? null : fecha.withOffsetSameInstant(ZoneOffset.UTC);
    }
}
