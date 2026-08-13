package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.aplicacion;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.consultaraplicacion.primaryports.interactor.ConsultarAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.CrearAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.publisher.CrearAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.BusinessException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto.ActualizarAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.interactor.ActualizarAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.primaryports.interactor.EliminarAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.publisher.EliminarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.secondaryports.event.AplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.aplicacion.AplicacionResponse;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/aplicaciones")
public final class AplicacionController {

    private final CrearAplicacionInteractor crearAplicacionInteractor;
    private final ConsultarAplicacionInteractor consultarAplicacionInteractor;
    private final CrearAplicacionPublisher crearAplicacionPublisher;
    private final ActualizarAplicacionInteractor actualizarAplicacionInteractor;
    private final ActualizarAplicacionPublisher actualizarAplicacionPublisher;
    private final EliminarAplicacionInteractor eliminarAplicacionInteractor;
    private final EliminarAplicacionPublisher eliminarAplicacionPublisher;

    public AplicacionController(final CrearAplicacionInteractor crearAplicacionInteractor,
                                final ConsultarAplicacionInteractor consultarAplicacionInteractor,
                                final CrearAplicacionPublisher crearAplicacionPublisher,
                                final ActualizarAplicacionInteractor actualizarAplicacionInteractor,
                                final ActualizarAplicacionPublisher actualizarAplicacionPublisher,
                                final EliminarAplicacionInteractor eliminarAplicacionInteractor,
                                final EliminarAplicacionPublisher eliminarAplicacionPublisher) {
        this.crearAplicacionInteractor = crearAplicacionInteractor;
        this.consultarAplicacionInteractor = consultarAplicacionInteractor;
        this.crearAplicacionPublisher = crearAplicacionPublisher;
        this.actualizarAplicacionInteractor = actualizarAplicacionInteractor;
        this.actualizarAplicacionPublisher = actualizarAplicacionPublisher;
        this.eliminarAplicacionInteractor = eliminarAplicacionInteractor;
        this.eliminarAplicacionPublisher = eliminarAplicacionPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AplicacionEvent>> publicarEventos() {
        var crearEventos = crearAplicacionPublisher.getStream().cast(AplicacionEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("aplicacion")
                        .build());
        var actualizarEventos = actualizarAplicacionPublisher.getStream().cast(AplicacionEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("aplicacion")
                        .build());
        var eliminarEventos = eliminarAplicacionPublisher.getStream().cast(AplicacionEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("aplicacion")
                        .build());

        return Flux.concat(
                Mono.just(ServerSentEvent.<AplicacionEvent>builder()
                        .comment("connected")
                        .build()),
                crearEventos,
                actualizarEventos,
                eliminarEventos
        );
    }

    @GetMapping
    public Mono<ResponseEntity<AplicacionResponse>> consultarTodasLasAplicaciones() {
        return Mono.fromCallable(() -> {
            var response = new AplicacionResponse();

            try {
                var aplicaciones = consultarAplicacionInteractor.execute();
                response.getAplicaciones().addAll(aplicaciones);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando las aplicaciones.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AplicacionResponse>> consultarAplicacionesPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new AplicacionResponse();

            try {
                var aplicaciones = consultarAplicacionInteractor.execute(id);
                response.getAplicaciones().addAll(aplicaciones);

                if (aplicaciones.isEmpty()) {
                    response.getMensajes().add("No se encontro la aplicacion con el id especificado.");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando la aplicacion.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping
    public Mono<ResponseEntity<AplicacionResponse>> crear(@RequestBody final CrearAplicacionDtoRequest aplicacion) {
        return Mono.fromCallable(() -> {
            var response = new AplicacionResponse();
            try {
                crearAplicacionInteractor.execute(aplicacion);
                response.getMensajes().add("Aplicacion creada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error creando la aplicacion.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AplicacionResponse>> actualizar(@PathVariable final UUID id,
                                                               @RequestBody final ActualizarAplicacionDtoRequest aplicacion) {
        return Mono.fromCallable(() -> {
            var response = new AplicacionResponse();
            try {
                actualizarAplicacionInteractor.execute(id, aplicacion);
                response.getMensajes().add("Aplicacion actualizada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error actualizando la aplicacion.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<AplicacionResponse>> eliminar(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new AplicacionResponse();
            try {
                eliminarAplicacionInteractor.execute(id);
                response.getMensajes().add("Aplicacion eliminada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error eliminando la aplicacion.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
