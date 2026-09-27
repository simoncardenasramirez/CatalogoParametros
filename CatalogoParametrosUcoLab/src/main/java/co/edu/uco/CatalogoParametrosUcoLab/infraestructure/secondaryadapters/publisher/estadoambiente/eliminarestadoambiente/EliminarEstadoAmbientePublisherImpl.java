package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.estadoambiente.eliminarestadoambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.secondaryports.event.EliminarEstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.secondaryports.publisher.EliminarEstadoAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class EliminarEstadoAmbientePublisherImpl implements EliminarEstadoAmbientePublisher {
    private final Sinks.Many<EliminarEstadoAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final EliminarEstadoAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<EliminarEstadoAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
