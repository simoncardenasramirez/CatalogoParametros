package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import java.time.OffsetDateTime;

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
        return OffsetDateTime.parse(text);
    }

    public static String format(final OffsetDateTime dateTime) {
        return dateTime == null ? "null" : "'" + dateTime + "'";
    }
}
