package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class RangoFechasIsValidRuleTest {

    @Test
    void debeRechazarFechaFinalAnteriorALaInicial() {
        var fechaInicio = OffsetDateTime.of(2026, 12, 31, 23, 59, 59, 0, ZoneOffset.of("-05:00"));
        assertThrows(ValidationException.class,
                () -> RangoFechasIsValidRule.execute(fechaInicio, fechaInicio.minusSeconds(1)));
    }

    @Test
    void debePermitirFechaFinalIgualOPosterior() {
        var fechaInicio = OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.of("-05:00"));
        assertDoesNotThrow(() -> RangoFechasIsValidRule.execute(fechaInicio, fechaInicio));
        assertDoesNotThrow(() -> RangoFechasIsValidRule.execute(fechaInicio, fechaInicio.plusDays(1)));
    }
}
