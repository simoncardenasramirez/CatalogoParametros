package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class RangoFechasIsValidRuleTest {

    @Test
    void debeRechazarFechaFinalAnteriorALaInicial() {
        var fechaInicio = LocalDateTime.of(2026, 12, 31, 23, 59, 59);
        assertThrows(ValidationException.class,
                () -> RangoFechasIsValidRule.execute(fechaInicio, fechaInicio.minusSeconds(1)));
    }

    @Test
    void debePermitirFechaFinalIgualOPosterior() {
        var fechaInicio = LocalDateTime.of(2026, 1, 1, 0, 0);
        assertDoesNotThrow(() -> RangoFechasIsValidRule.execute(fechaInicio, fechaInicio));
        assertDoesNotThrow(() -> RangoFechasIsValidRule.execute(fechaInicio, fechaInicio.plusDays(1)));
    }
}
