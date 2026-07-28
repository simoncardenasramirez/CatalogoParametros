package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.funcionalidad.crearfuncionalidad;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.publisher.CrearFuncionalidadPublisher;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.event.CrearFuncionalidadEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class CrearFuncionalidadPublisherImpl implements CrearFuncionalidadPublisher {

    private final Sinks.Many<CrearFuncionalidadEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final CrearFuncionalidadEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<CrearFuncionalidadEvent> getStream() {
        return sink.asFlux();
    }
}
