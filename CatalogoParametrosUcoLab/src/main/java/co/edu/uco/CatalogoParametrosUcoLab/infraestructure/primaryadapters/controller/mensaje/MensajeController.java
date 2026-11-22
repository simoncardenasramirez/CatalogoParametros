package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.mensaje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.message.ConsultarMensajeDetalleInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambio;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambioPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.mensaje.MensajeResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.springframework.http.codec.ServerSentEvent;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/mensajes")
public final class MensajeController {

    private final ConsultarMensajeDetalleInteractor consultarMensajeDetalleInteractor;
    private final MensajeCambioPublisher mensajeCambioPublisher;

    public MensajeController(final ConsultarMensajeDetalleInteractor consultarMensajeDetalleInteractor) {
        this(consultarMensajeDetalleInteractor, null);
    }

    @Autowired
    public MensajeController(final ConsultarMensajeDetalleInteractor consultarMensajeDetalleInteractor,
            final MensajeCambioPublisher mensajeCambioPublisher) {
        this.consultarMensajeDetalleInteractor = consultarMensajeDetalleInteractor;
        this.mensajeCambioPublisher = mensajeCambioPublisher;
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<MensajeCambio>> eventos() {
        if (mensajeCambioPublisher == null) {
            return Flux.just(ServerSentEvent.<MensajeCambio>builder().comment("connected").build());
        }
        return Flux.concat(
                Mono.just(ServerSentEvent.<MensajeCambio>builder().comment("connected").build()),
                mensajeCambioPublisher.getStream().map(evento -> ServerSentEvent.builder(evento)
                        .event(evento.eventType())
                        .id(evento.eventId())
                        .build()));
    }

    @GetMapping("/{codigo}")
    public Mono<ResponseEntity<MensajeResponse>> consultarMensaje(@PathVariable final String codigo) {
        return Mono.fromCallable(() -> {
            final MensajeDetalle mensaje = consultarMensajeDetalleInteractor.execute(codigo);
            final MensajeResponse response = new MensajeResponse(
                    mensaje.code(),
                    mensaje.title(),
                    mensaje.content(),
                    mensaje.type(),
                    mensaje.category(),
                    mensaje.functionality());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
