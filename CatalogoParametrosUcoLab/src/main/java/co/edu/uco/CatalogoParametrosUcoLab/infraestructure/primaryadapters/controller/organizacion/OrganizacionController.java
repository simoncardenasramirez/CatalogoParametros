package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.organizacion;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.CrearOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.publisher.CrearOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.exception.OrganizacionException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.secondaryports.event.OrganizacionEvent;
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
@RequestMapping("/catalogo-parametros/api/v1/organizaciones")
public final class OrganizacionController {

    private final CrearOrganizacionInteractor crearOrganizacionInteractor;
    private final CrearOrganizacionPublisher crearOrganizacionPublisher;

    public OrganizacionController(final CrearOrganizacionInteractor crearOrganizacionInteractor,
            final CrearOrganizacionPublisher crearOrganizacionPublisher) {
        this.crearOrganizacionInteractor = crearOrganizacionInteractor;
        this.crearOrganizacionPublisher = crearOrganizacionPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<OrganizacionEvent>> publicarEventos() {
        var eventos = crearOrganizacionPublisher.getStream().cast(OrganizacionEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("organizacion")
                        .build());

        return Flux.concat(Mono.just(ServerSentEvent.<OrganizacionEvent>builder()
                .comment("connected")
                .build()), eventos);
    }

    @PostMapping
    public Mono<ResponseEntity<String>> crear(@RequestBody final CrearOrganizacionDto organizacion) {
        return Mono.fromCallable(() -> {
            try {
                crearOrganizacionInteractor.execute(organizacion);
                return new ResponseEntity<>("Organizacion creada exitosamente.", HttpStatus.CREATED);
            } catch (final OrganizacionException exception) {
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (final Exception exception) {
                return new ResponseEntity<>("Ocurrio un error creando la organizacion: " + exception.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
