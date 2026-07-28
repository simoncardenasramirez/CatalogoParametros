package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.modulo;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.CrearModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.event.CrearModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.publisher.CrearModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.exception.ModuloException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.secondaryports.event.ModuloEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametroResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/modulos")
public final class ModuloController {

    private final CrearModuloInteractor crearModuloInteractor;
    private final CrearModuloPublisher crearModuloPublisher;

    public ModuloController(final CrearModuloInteractor crearModuloInteractor,
            final CrearModuloPublisher crearModuloPublisher) {
        this.crearModuloInteractor = crearModuloInteractor;
        this.crearModuloPublisher = crearModuloPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ModuloEvent>> publicarEventos() {
        var eventos = crearModuloPublisher.getStream().cast(ModuloEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("modulo")
                        .build());

        return Flux.concat(Mono.just(ServerSentEvent.<ModuloEvent>builder()
                .comment("connected")
                .build()), eventos);
    }

    @PostMapping
    public Mono<ResponseEntity<ParametroResponse>> crear(@RequestBody final CrearModuloDto modulo) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                crearModuloInteractor.execute(modulo);
                response.getMensajes().add("Modulo creado exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (final ModuloException exception) {
                response.getMensajes().add(exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error creando el modulo: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
