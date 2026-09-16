package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class ValidateHelperTest {

    @Test
    void debeRechazarFechaFinalAnteriorALaFechaInicio() {
        var inicio = OffsetDateTime.of(2026, 8, 26, 10, 0, 0, 0, ZoneOffset.of("-05:00"));
        var finalAnterior = inicio.minusSeconds(1);

        var exception = assertThrows(ValidationException.class,
                () -> ValidateHelper.validateRangoFechas(inicio, finalAnterior));

        assertEquals("La fecha final no puede ser anterior a la fecha de inicio.", exception.getMessage());
    }

    @Test
    void debePermitirFechasIguales() {
        var fecha = OffsetDateTime.of(2026, 8, 26, 10, 0, 0, 0, ZoneOffset.of("-05:00"));
        assertDoesNotThrow(() -> ValidateHelper.validateRangoFechas(fecha, fecha));
    }

    @Test
    void debePermitirFechaFinalPosterior() {
        var inicio = OffsetDateTime.of(2026, 8, 26, 10, 0, 0, 0, ZoneOffset.of("-05:00"));
        assertDoesNotThrow(() -> ValidateHelper.validateRangoFechas(inicio, inicio.plusDays(1)));
    }

    @Test
    void debePermitirFechasOpcionales() {
        assertDoesNotThrow(() -> ValidateHelper.validateRangoFechas(null, null));
    }
}
