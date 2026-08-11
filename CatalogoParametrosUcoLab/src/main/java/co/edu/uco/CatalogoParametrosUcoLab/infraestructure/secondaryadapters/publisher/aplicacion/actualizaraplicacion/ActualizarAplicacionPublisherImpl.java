package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.aplicacion.actualizaraplicacion;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.event.ActualizarAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class ActualizarAplicacionPublisherImpl implements ActualizarAplicacionPublisher {

    private final Sinks.Many<ActualizarAplicacionEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final ActualizarAplicacionEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<ActualizarAplicacionEvent> getStream() {
        return sink.asFlux();
    }
}
