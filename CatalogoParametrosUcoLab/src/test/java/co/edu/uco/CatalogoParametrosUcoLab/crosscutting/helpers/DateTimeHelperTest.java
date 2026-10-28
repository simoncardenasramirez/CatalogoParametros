package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.ObjectMapper;

class DateTimeHelperTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void debeParsearFormatosDeSurrealDb() {
        var esperado = OffsetDateTime.of(2026, 1, 2, 10, 30, 0, 0, ZoneOffset.of("-05:00"));
        assertEquals(esperado.withOffsetSameInstant(ZoneOffset.UTC), DateTimeHelper.parse(mapper.readTree("\"2026-01-02T10:30-05:00\"")));
        assertEquals(esperado.withOffsetSameInstant(ZoneOffset.UTC), DateTimeHelper.parse(mapper.readTree("\"d'2026-01-02T10:30:00-05:00'\"")));
    }

    @Test
    void debeRetornarNuloParaFechaAusente() {
        assertNull(DateTimeHelper.parse(null));
        assertNull(DateTimeHelper.parse(mapper.readTree("null")));
        assertNull(DateTimeHelper.parse(mapper.readTree("\"\"")));
    }

    @Test
    void debeFormatearFechaYNulo() {
        var fecha = OffsetDateTime.of(2026, 1, 2, 10, 30, 0, 0, ZoneOffset.of("-05:00"));
        assertEquals("'2026-01-02T15:30:00Z'", DateTimeHelper.format(fecha));
        assertEquals("null", DateTimeHelper.format(null));
    }
    @Test
    void debeInterpretarHistoricoSinZonaComoUtcCuandoLeePersistencia() {
        assertEquals(OffsetDateTime.parse("2026-01-02T10:30:00Z"),
                DateTimeHelper.parse(mapper.readTree("\"2026-01-02T10:30:00\"")));
    }
    @Test
    void debeConservarInstanteYPrecisionCuandoNormalizaUnDesfase() {
        var fecha = OffsetDateTime.parse("2026-07-15T18:35:42.123456789+05:30");
        var utc = DateTimeHelper.toUtc(fecha);
        assertEquals(fecha.toInstant(), utc.toInstant());
        assertEquals(ZoneOffset.UTC, utc.getOffset());
        assertEquals("'2026-07-15T13:05:42.123456789Z'", DateTimeHelper.format(fecha));
    }
}
