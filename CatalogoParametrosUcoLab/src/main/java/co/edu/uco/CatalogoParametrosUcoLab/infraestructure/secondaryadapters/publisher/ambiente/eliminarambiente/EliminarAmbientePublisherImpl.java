package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.ambiente.eliminarambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.secondaryports.event.EliminarAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.secondaryports.publisher.EliminarAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class EliminarAmbientePublisherImpl implements EliminarAmbientePublisher {
    private final Sinks.Many<EliminarAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final EliminarAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<EliminarAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
