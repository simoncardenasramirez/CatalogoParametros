package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.modulo.crearmodulo;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.publisher.CrearModuloPublisher;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.event.CrearModuloEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class CrearModuloPublisherImpl implements CrearModuloPublisher {

    private final Sinks.Many<CrearModuloEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final CrearModuloEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<CrearModuloEvent> getStream() {
        return sink.asFlux();
    }
}
