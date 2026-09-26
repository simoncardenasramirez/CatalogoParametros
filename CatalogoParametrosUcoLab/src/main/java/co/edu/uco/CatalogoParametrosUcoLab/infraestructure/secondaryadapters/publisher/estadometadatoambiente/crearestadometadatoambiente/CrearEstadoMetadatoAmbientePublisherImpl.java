package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.estadometadatoambiente.crearestadometadatoambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.secondaryports.event.CrearEstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.secondaryports.publisher.CrearEstadoMetadatoAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class CrearEstadoMetadatoAmbientePublisherImpl implements CrearEstadoMetadatoAmbientePublisher {
    private final Sinks.Many<CrearEstadoMetadatoAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final CrearEstadoMetadatoAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<CrearEstadoMetadatoAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
