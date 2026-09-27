package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.estadometadatoambiente.actualizarestadometadatoambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.secondaryports.event.ActualizarEstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.secondaryports.publisher.ActualizarEstadoMetadatoAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class ActualizarEstadoMetadatoAmbientePublisherImpl implements ActualizarEstadoMetadatoAmbientePublisher {
    private final Sinks.Many<ActualizarEstadoMetadatoAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final ActualizarEstadoMetadatoAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<ActualizarEstadoMetadatoAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
