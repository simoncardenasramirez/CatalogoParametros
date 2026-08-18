package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.funcionalidad.eliminarfuncionalidad;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.event.EliminarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.publisher.EliminarFuncionalidadPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class EliminarFuncionalidadPublisherImpl implements EliminarFuncionalidadPublisher {

    private final Sinks.Many<EliminarFuncionalidadEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final EliminarFuncionalidadEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<EliminarFuncionalidadEvent> getStream() {
        return sink.asFlux();
    }
}
