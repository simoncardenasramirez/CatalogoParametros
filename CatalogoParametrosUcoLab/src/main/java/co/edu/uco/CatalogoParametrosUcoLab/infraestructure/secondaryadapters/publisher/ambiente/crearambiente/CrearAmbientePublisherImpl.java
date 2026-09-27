package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.ambiente.crearambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.secondaryports.event.CrearAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.secondaryports.publisher.CrearAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class CrearAmbientePublisherImpl implements CrearAmbientePublisher {
    private final Sinks.Many<CrearAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final CrearAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<CrearAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
