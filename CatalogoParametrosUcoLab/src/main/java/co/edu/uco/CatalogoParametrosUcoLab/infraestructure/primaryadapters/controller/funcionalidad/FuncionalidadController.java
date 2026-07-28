package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.funcionalidad;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.ConsultarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.CrearFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.event.CrearFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.publisher.CrearFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.secondaryports.event.FuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.funcionalidad.FuncionalidadResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametroResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/funcionalidades")
public final class FuncionalidadController {

    private final CrearFuncionalidadInteractor crearFuncionalidadInteractor;
    private final ConsultarFuncionalidadInteractor consultarFuncionalidadInteractor;
    private final CrearFuncionalidadPublisher crearFuncionalidadPublisher;

    public FuncionalidadController(final CrearFuncionalidadInteractor crearFuncionalidadInteractor,
            final ConsultarFuncionalidadInteractor consultarFuncionalidadInteractor,
            final CrearFuncionalidadPublisher crearFuncionalidadPublisher) {
        this.crearFuncionalidadInteractor = crearFuncionalidadInteractor;
        this.consultarFuncionalidadInteractor = consultarFuncionalidadInteractor;
        this.crearFuncionalidadPublisher = crearFuncionalidadPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<FuncionalidadEvent>> publicarEventos() {
        var eventos = crearFuncionalidadPublisher.getStream().cast(FuncionalidadEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("funcionalidad")
                        .build());

        return Flux.concat(Mono.just(ServerSentEvent.<FuncionalidadEvent>builder()
                .comment("connected")
                .build()), eventos);
    }

    @PostMapping
    public Mono<ResponseEntity<ParametroResponse>> crearFuncionalidad(@RequestBody final CrearFuncionalidadDto funcionalidad) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                crearFuncionalidadInteractor.execute(funcionalidad);
                response.getMensajes().add("Funcionalidad creada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (final FuncionalidadException exception) {
                response.getMensajes().add(exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error creando la funcionalidad: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    public Mono<ResponseEntity<FuncionalidadResponse>> consultarTodasLasFuncionalidades() {
        return Mono.fromCallable(() -> {
            var response = new FuncionalidadResponse();

            try {
                var funcionalidades = consultarFuncionalidadInteractor.execute();
                response.getFuncionalidades().addAll(funcionalidades);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando las funcionalidades.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<FuncionalidadResponse>> consultarFuncionalidadesPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new FuncionalidadResponse();

            try {
                var funcionalidades = consultarFuncionalidadInteractor.execute(id);
                response.getFuncionalidades().addAll(funcionalidades);

                if (funcionalidades.isEmpty()) {
                    response.getMensajes().add("No se encontro la funcionalidad con el id especificado.");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando la funcionalidad.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}