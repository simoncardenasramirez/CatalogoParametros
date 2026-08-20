package co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

class TelemetryServiceTest {

    private TelemetryService telemetryService;

    @BeforeEach
    void setUp() {
        telemetryService = new TelemetryService(new SimpleMeterRegistry());
    }

    @Test
    void debeEjecutarElRunnableCuandoSeRegistraUnaOperacionDeNegocio() {
        var ejecutado = new AtomicBoolean(false);
        Runnable operacion = () -> ejecutado.set(true);

        telemetryService.recordBusinessOperation("crear.parametro", operacion);

        assertTrue(ejecutado.get());
    }

    @Test
    void debeDevolverElValorDelSupplierCuandoSeRegistraUnaOperacionDeNegocio() {
        var meterRegistry = new SimpleMeterRegistry();
        var servicio = new TelemetryService(meterRegistry);

        var valor = servicio.recordBusinessOperation("consultar.parametro", () -> "resultado");

        assertEquals("resultado", valor);
        var contador = meterRegistry.find("business.operation.count").tag("operation", "consultar.parametro").counter();
        assertNotNull(contador);
        assertEquals(1.0, contador.count());
    }

    @Test
    void debeIncrementarElContadorDeOperacionesCuandoSeRegistraUnaOperacionConRunnable() {
        var meterRegistry = new SimpleMeterRegistry();
        var servicio = new TelemetryService(meterRegistry);
        var operacion = "crear.parametro";

        servicio.recordBusinessOperation(operacion, () -> {
            // operacion de negocio simulada
        });

        var contador = meterRegistry.find("business.operation.count").tag("operation", operacion).counter();
        assertNotNull(contador);
        assertEquals(1.0, contador.count());
    }

    @Test
    void debeIncrementarElContadorDeErroresDeNegocioCuandoSeRegistraUnError() {
        var meterRegistry = new SimpleMeterRegistry();
        var servicio = new TelemetryService(meterRegistry);
        var excepcion = new IllegalStateException("error de negocio");

        servicio.recordBusinessError("crear.parametro", excepcion);

        var contador = meterRegistry.find("business.operation.errors")
                .tag("operation", "crear.parametro")
                .tag("error", "IllegalStateException")
                .counter();
        assertNotNull(contador);
        assertEquals(1.0, contador.count());
    }

    @Test
    void debeIncrementarElContadorDeErroresDeAplicacionCuandoSeRegistraUnError() {
        var meterRegistry = new SimpleMeterRegistry();
        var servicio = new TelemetryService(meterRegistry);

        servicio.recordError("VALIDATION", "mensaje de error");

        var contador = meterRegistry.find("application.error.count").tag("errorType", "VALIDATION").counter();
        assertNotNull(contador);
        assertEquals(1.0, contador.count());
    }

    @Test
    void debeIniciarUnTimerCuandoSeSolicitaUnaOperacionCronometrada() {
        var muestra = telemetryService.startOperationTimer();
        assertNotNull(muestra);
    }

    @Test
    void debeDetenerElTimerSinLanzarExcepcionesCuandoSeCronometraUnaOperacion() {
        var muestra = telemetryService.startOperationTimer();

        telemetryService.stopOperationTimer(muestra, "crear.parametro");
    }

    @Test
    void debeEjecutarElSupplierYDevolverElMismoValorCuandoSeRegistraUnaOperacion() {
        var resultadoEsperado = "valor de retorno";
        var resultado = telemetryService.recordBusinessOperation("operacion", () -> resultadoEsperado);

        assertSame(resultadoEsperado, resultado);
    }
}