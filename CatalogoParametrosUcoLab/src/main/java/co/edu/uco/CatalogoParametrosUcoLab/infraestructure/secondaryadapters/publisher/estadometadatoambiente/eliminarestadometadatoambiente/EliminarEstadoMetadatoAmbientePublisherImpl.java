package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.estadometadatoambiente.eliminarestadometadatoambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.secondaryports.event.EliminarEstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.secondaryports.publisher.EliminarEstadoMetadatoAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class EliminarEstadoMetadatoAmbientePublisherImpl implements EliminarEstadoMetadatoAmbientePublisher {
    private final Sinks.Many<EliminarEstadoMetadatoAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final EliminarEstadoMetadatoAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<EliminarEstadoMetadatoAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
