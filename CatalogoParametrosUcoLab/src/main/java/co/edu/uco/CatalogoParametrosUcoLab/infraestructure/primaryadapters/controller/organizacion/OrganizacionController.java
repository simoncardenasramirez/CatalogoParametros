package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.organizacion;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.ActualizarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.consultarorganizacion.primaryports.interactor.ConsultarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.publisher.ActualizarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.CrearOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.publisher.CrearOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.primaryports.interactor.EliminarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.publisher.EliminarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.secondaryports.event.OrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.organizacion.OrganizacionResponse;
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
@RequestMapping("/catalogo-parametros/api/v1/organizaciones")
public final class OrganizacionController {

    private final CrearOrganizacionInteractor crearOrganizacionInteractor;
    private final CrearOrganizacionPublisher crearOrganizacionPublisher;
    private final ActualizarOrganizacionInteractor actualizarOrganizacionInteractor;
    private final ActualizarOrganizacionPublisher actualizarOrganizacionPublisher;
    private final EliminarOrganizacionInteractor eliminarOrganizacionInteractor;
    private final EliminarOrganizacionPublisher eliminarOrganizacionPublisher;
    private final ConsultarOrganizacionInteractor consultarOrganizacionInteractor;

    public OrganizacionController(final CrearOrganizacionInteractor crearOrganizacionInteractor,
            final CrearOrganizacionPublisher crearOrganizacionPublisher,
            final ActualizarOrganizacionInteractor actualizarOrganizacionInteractor,
            final ActualizarOrganizacionPublisher actualizarOrganizacionPublisher,
            final EliminarOrganizacionInteractor eliminarOrganizacionInteractor,
            final EliminarOrganizacionPublisher eliminarOrganizacionPublisher,
            final ConsultarOrganizacionInteractor consultarOrganizacionInteractor) {
        this.crearOrganizacionInteractor = crearOrganizacionInteractor;
        this.crearOrganizacionPublisher = crearOrganizacionPublisher;
        this.actualizarOrganizacionInteractor = actualizarOrganizacionInteractor;
        this.actualizarOrganizacionPublisher = actualizarOrganizacionPublisher;
        this.eliminarOrganizacionInteractor = eliminarOrganizacionInteractor;
        this.eliminarOrganizacionPublisher = eliminarOrganizacionPublisher;
        this.consultarOrganizacionInteractor = consultarOrganizacionInteractor;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<OrganizacionEvent>> publicarEventos() {
        var crearEventos = crearOrganizacionPublisher.getStream().cast(OrganizacionEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("organizacion")
                        .build());
        var actualizarEventos = actualizarOrganizacionPublisher.getStream().cast(OrganizacionEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("organizacion")
                        .build());
        var eliminarEventos = eliminarOrganizacionPublisher.getStream().cast(OrganizacionEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("organizacion")
                        .build());

        return Flux.concat(
                Mono.just(ServerSentEvent.<OrganizacionEvent>builder()
                        .comment("connected")
                        .build()),
                crearEventos,
                actualizarEventos,
                eliminarEventos
        );
    }

    @PostMapping
    public Mono<ResponseEntity<OrganizacionResponse>> crear(@RequestBody final CrearOrganizacionDto organizacion) {
        return Mono.fromCallable(() -> {
            var response = new OrganizacionResponse();
            try {
                crearOrganizacionInteractor.execute(organizacion);
                response.getMensajes().add("Organizacion creada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error creando la organizacion: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<OrganizacionResponse>> actualizar(@PathVariable final UUID id,
            @RequestBody final ActualizarOrganizacionDto organizacion) {
        return Mono.fromCallable(() -> {
            var response = new OrganizacionResponse();
            try {
                var dto = new ActualizarOrganizacionDto(id, organizacion.getNombre());
                actualizarOrganizacionInteractor.execute(dto);
                response.getMensajes().add("Organizacion actualizada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error actualizando la organizacion: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<OrganizacionResponse>> eliminar(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new OrganizacionResponse();
            try {
                eliminarOrganizacionInteractor.execute(id);
                response.getMensajes().add("Organizacion eliminada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error eliminando la organizacion: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    public Mono<ResponseEntity<OrganizacionResponse>> consultarTodasLasOrganizaciones() {
        return Mono.fromCallable(() -> {
            var response = new OrganizacionResponse();
            try {
                var organizaciones = consultarOrganizacionInteractor.execute();
                response.getOrganizaciones().addAll(organizaciones);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando las organizaciones: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrganizacionResponse>> consultarOrganizacionPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new OrganizacionResponse();
            try {
                var organizaciones = consultarOrganizacionInteractor.execute(id);
                response.getOrganizaciones().addAll(organizaciones);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando la organizacion: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
