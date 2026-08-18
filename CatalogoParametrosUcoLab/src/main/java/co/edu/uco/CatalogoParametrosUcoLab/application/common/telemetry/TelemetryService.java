package co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class TelemetryService {

    private final MeterRegistry meterRegistry;

    public TelemetryService(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public <T> T recordBusinessOperation(final String operationName, final Supplier<T> supplier) {
        Counter.builder("business.operation.count")
                .description("Total de operaciones de negocio por tipo")
                .tag("operation", operationName)
                .register(meterRegistry)
                .increment();
        return supplier.get();
    }

    public void recordBusinessOperation(final String operationName, final Runnable runnable) {
        Counter.builder("business.operation.count")
                .description("Total de operaciones de negocio por tipo")
                .tag("operation", operationName)
                .register(meterRegistry)
                .increment();
        runnable.run();
    }

    public void recordBusinessError(final String operationName, final Throwable exception) {
        Counter.builder("business.operation.errors")
                .description("Total de errores por tipo de operación")
                .tag("operation", operationName)
                .tag("error", exception.getClass().getSimpleName())
                .register(meterRegistry)
                .increment();
    }

    public void recordError(final String errorType, final String message) {
        Counter.builder("application.error.count")
                .description("Total de errores de aplicación por tipo")
                .tag("errorType", errorType)
                .register(meterRegistry)
                .increment();
    }

    public Timer.Sample startOperationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopOperationTimer(final Timer.Sample sample, final String operationName) {
        sample.stop(Timer.builder("business.operation.duration")
                .description("Duración de operaciones de negocio en nanosegundos")
                .tag("operation", operationName)
                .register(meterRegistry));
    }
}
