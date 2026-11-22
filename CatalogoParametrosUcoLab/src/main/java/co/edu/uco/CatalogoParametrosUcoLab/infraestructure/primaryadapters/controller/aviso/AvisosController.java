package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.aviso;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambio;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambioPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Expone los avisos recibidos desde MessageUcoLab a las pantallas consumidoras. */
@RestController
@RequestMapping("/catalogo-parametros/api/v1/avisos")
public final class AvisosController {

    private final MensajeCambioPublisher avisos;

    public AvisosController(final MensajeCambioPublisher avisos) {
        this.avisos = avisos;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<MensajeCambio>> avisos() {
        return Flux.concat(
                Mono.just(ServerSentEvent.<MensajeCambio>builder().comment("connected").build()),
                avisos.getStream().map(aviso -> ServerSentEvent.builder(aviso)
                        .event("aviso")
                        .id(aviso.eventId())
                        .build()));
    }
}
