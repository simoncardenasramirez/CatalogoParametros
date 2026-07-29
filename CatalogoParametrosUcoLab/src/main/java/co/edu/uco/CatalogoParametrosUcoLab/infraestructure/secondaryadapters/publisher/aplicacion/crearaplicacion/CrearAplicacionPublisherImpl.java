package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.aplicacion.crearaplicacion;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.publisher.CrearAplicacionPublisher;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.event.CrearAplicacionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class CrearAplicacionPublisherImpl implements CrearAplicacionPublisher {

    private final Sinks.Many<CrearAplicacionEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final CrearAplicacionEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<CrearAplicacionEvent> getStream() {
        return sink.asFlux();
    }
}
