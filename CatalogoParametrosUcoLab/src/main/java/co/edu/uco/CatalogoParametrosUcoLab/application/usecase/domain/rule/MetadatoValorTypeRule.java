package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import tools.jackson.databind.JsonNode;

public final class MetadatoValorTypeRule {

    private MetadatoValorTypeRule() {
    }

    public static void execute(final String tipo, final JsonNode valor) {
        if (valor == null || valor.isNull() || valor.isMissingNode()
                || valor.isTextual() && TextHelper.isBlank(valor.asText())) {
            throw ValidationException.build("El valor del metadato es obligatorio.");
        }

        if ("json".equalsIgnoreCase(tipo) && !valor.isObject() && !valor.isArray()) {
            throw ValidationException.build("El valor debe ser un objeto o arreglo JSON.");
        }
        if ("alfanumerico".equalsIgnoreCase(tipo) && !valor.isTextual()) {
            throw ValidationException.build("El valor del metadato alfanumerico debe ser una cadena.");
        }
        if ("date".equalsIgnoreCase(tipo)) {
            validateDate(valor);
        }
    }

    private static void validateDate(final JsonNode valor) {
        if (!valor.isTextual()) {
            throw ValidationException.build("El valor del metadato date debe ser una fecha en formato yyyy-MM-dd.");
        }
        try {
            LocalDate.parse(valor.asText());
        } catch (final DateTimeParseException exception) {
            throw ValidationException.build("El valor del metadato date debe ser una fecha en formato yyyy-MM-dd.");
        }
    }
}
