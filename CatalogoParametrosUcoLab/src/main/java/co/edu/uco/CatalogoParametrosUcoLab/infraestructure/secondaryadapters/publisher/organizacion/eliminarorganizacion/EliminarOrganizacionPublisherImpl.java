package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.organizacion.eliminarorganizacion;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.publisher.EliminarOrganizacionPublisher;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.event.EliminarOrganizacionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class EliminarOrganizacionPublisherImpl implements EliminarOrganizacionPublisher {

    private final Sinks.Many<EliminarOrganizacionEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final EliminarOrganizacionEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<EliminarOrganizacionEvent> getStream() {
        return sink.asFlux();
    }
}
