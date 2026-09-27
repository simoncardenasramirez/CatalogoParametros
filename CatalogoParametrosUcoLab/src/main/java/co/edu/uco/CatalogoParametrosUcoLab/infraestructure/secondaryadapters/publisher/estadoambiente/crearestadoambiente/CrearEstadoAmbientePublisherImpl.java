package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.estadoambiente.crearestadoambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.secondaryports.event.CrearEstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.secondaryports.publisher.CrearEstadoAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class CrearEstadoAmbientePublisherImpl implements CrearEstadoAmbientePublisher {
    private final Sinks.Many<CrearEstadoAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final CrearEstadoAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<CrearEstadoAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
