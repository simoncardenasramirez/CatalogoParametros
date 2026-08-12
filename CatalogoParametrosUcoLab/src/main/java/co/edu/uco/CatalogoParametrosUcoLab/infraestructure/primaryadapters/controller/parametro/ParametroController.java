package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.parametro;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.ActualizarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametro.primaryports.interactor.ConsultarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.BusinessException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.primaryports.interactor.EliminarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.publisher.EliminarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.secondaryports.event.ParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametroResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/parametros")
public final class ParametroController {

    private final CrearParametroInteractor crearParametroInteractor;
    private final ActualizarParametroInteractor actualizarParametroInteractor;
    private final EliminarParametroInteractor eliminarParametroInteractor;
    private final ConsultarParametroInteractor consultarParametroInteractor;
    private final CrearParametroPublisher crearParametroPublisher;
    private final ActualizarParametroPublisher actualizarParametroPublisher;
    private final EliminarParametroPublisher eliminarParametroPublisher;

    public ParametroController(final CrearParametroInteractor crearParametroInteractor,
            final ActualizarParametroInteractor actualizarParametroInteractor,
            final EliminarParametroInteractor eliminarParametroInteractor,
            final ConsultarParametroInteractor consultarParametroInteractor,
            final CrearParametroPublisher crearParametroPublisher,
            final ActualizarParametroPublisher actualizarParametroPublisher,
            final EliminarParametroPublisher eliminarParametroPublisher) {
        this.crearParametroInteractor = crearParametroInteractor;
        this.actualizarParametroInteractor = actualizarParametroInteractor;
        this.eliminarParametroInteractor = eliminarParametroInteractor;
        this.consultarParametroInteractor = consultarParametroInteractor;
        this.crearParametroPublisher = crearParametroPublisher;
        this.actualizarParametroPublisher = actualizarParametroPublisher;
        this.eliminarParametroPublisher = eliminarParametroPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ParametroEvent>> publicarEventos() {
        var eventos = Flux.merge(crearParametroPublisher.getStream().cast(ParametroEvent.class),
                actualizarParametroPublisher.getStream().cast(ParametroEvent.class),
                eliminarParametroPublisher.getStream().cast(ParametroEvent.class))
                .map(event -> ServerSentEvent.builder(event)
                        .event("parametro")
                        .build());

        return Flux.concat(Mono.just(ServerSentEvent.<ParametroEvent>builder()
                .comment("connected")
                .build()), eventos);
    }

    @PostMapping
    public Mono<ResponseEntity<ParametroResponse>> crearParametro(@RequestBody final CrearParametroDtoRequest parametro) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                crearParametroInteractor.execute(parametro);
                response.getMensajes().add("Parametro creado exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error creando el parametro.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ParametroResponse>> actualizarParametro(@PathVariable final UUID id,
            @RequestBody final ActualizarParametroDtoRequest parametro) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                actualizarParametroInteractor.execute(id, parametro);
                response.getMensajes().add("Parametro actualizado exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error actualizando el parametro.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ParametroResponse>> eliminarParametro(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                eliminarParametroInteractor.execute(id);
                response.getMensajes().add("Parametro eliminado exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error eliminando el parametro.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    public Mono<ResponseEntity<ParametroResponse>> consultarTodosLosParametros() {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                var parametros = consultarParametroInteractor.execute();
                response.getParametros().addAll(parametros);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando los parametros.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ParametroResponse>> consultarParametroPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                var parametros = consultarParametroInteractor.execute(id);
                response.getParametros().addAll(parametros);

                if (parametros.isEmpty()) {
                    response.getMensajes().add("No se encontro el parametro con el id especificado.");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando el parametro.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
