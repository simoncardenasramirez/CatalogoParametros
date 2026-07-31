package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.aplicacion;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.CrearAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.publisher.CrearAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.exception.AplicacionException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.secondaryports.event.AplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.aplicacion.AplicacionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/aplicaciones")
public final class AplicacionController {

    private final CrearAplicacionInteractor crearAplicacionInteractor;
    private final CrearAplicacionPublisher crearAplicacionPublisher;

    public AplicacionController(final CrearAplicacionInteractor crearAplicacionInteractor,
            final CrearAplicacionPublisher crearAplicacionPublisher) {
        this.crearAplicacionInteractor = crearAplicacionInteractor;
        this.crearAplicacionPublisher = crearAplicacionPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AplicacionEvent>> publicarEventos() {
        var eventos = crearAplicacionPublisher.getStream().cast(AplicacionEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("aplicacion")
                        .build());

        return Flux.concat(Mono.just(ServerSentEvent.<AplicacionEvent>builder()
                .comment("connected")
                .build()), eventos);
    }

    @PostMapping
    public Mono<ResponseEntity<AplicacionResponse>> crear(@RequestBody final CrearAplicacionDto aplicacion) {
        return Mono.fromCallable(() -> {
            var response = new AplicacionResponse();
            try {
                crearAplicacionInteractor.execute(aplicacion);
                response.getMensajes().add("Aplicacion creada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (final AplicacionException exception) {
                response.getMensajes().add(exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error creando la aplicacion.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
