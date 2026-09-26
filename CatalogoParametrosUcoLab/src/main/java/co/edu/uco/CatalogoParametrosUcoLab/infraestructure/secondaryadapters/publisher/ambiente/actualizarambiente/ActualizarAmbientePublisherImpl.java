package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.ambiente.actualizarambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.secondaryports.event.ActualizarAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.secondaryports.publisher.ActualizarAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class ActualizarAmbientePublisherImpl implements ActualizarAmbientePublisher {
    private final Sinks.Many<ActualizarAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final ActualizarAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<ActualizarAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
