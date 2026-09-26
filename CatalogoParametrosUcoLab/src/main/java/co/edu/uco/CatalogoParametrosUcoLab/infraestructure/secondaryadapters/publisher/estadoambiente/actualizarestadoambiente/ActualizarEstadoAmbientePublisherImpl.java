package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.estadoambiente.actualizarestadoambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.secondaryports.event.ActualizarEstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.secondaryports.publisher.ActualizarEstadoAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class ActualizarEstadoAmbientePublisherImpl implements ActualizarEstadoAmbientePublisher {
    private final Sinks.Many<ActualizarEstadoAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final ActualizarEstadoAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<ActualizarEstadoAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
