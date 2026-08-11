package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.modulo.actualizarmodulo;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.event.ActualizarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class ActualizarModuloPublisherImpl implements ActualizarModuloPublisher {

    private final Sinks.Many<ActualizarModuloEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final ActualizarModuloEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<ActualizarModuloEvent> getStream() {
        return sink.asFlux();
    }
}
