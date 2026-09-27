package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.ambiente;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.dto.ActualizarAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.interactor.ActualizarAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.consultarambiente.primaryports.interactor.ConsultarAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.dto.CrearAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.interactor.CrearAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.primaryports.interactor.EliminarAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.secondaryports.publisher.CrearAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.secondaryports.publisher.ActualizarAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.secondaryports.publisher.EliminarAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.secondaryports.event.AmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.ambiente.AmbienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/ambientes")
@Tag(name = "Ambientes", description = "Administracion de ambientes del catalogo.")
public final class AmbienteController {
    private final CrearAmbienteInteractor crearInteractor;
    private final ActualizarAmbienteInteractor actualizarInteractor;
    private final EliminarAmbienteInteractor eliminarInteractor;
    private final ConsultarAmbienteInteractor consultarInteractor;
    private final CrearAmbientePublisher crearPublisher;
    private final ActualizarAmbientePublisher actualizarPublisher;
    private final EliminarAmbientePublisher eliminarPublisher;

    public AmbienteController(final CrearAmbienteInteractor crearInteractor,
            final ActualizarAmbienteInteractor actualizarInteractor,
            final EliminarAmbienteInteractor eliminarInteractor,
            final ConsultarAmbienteInteractor consultarInteractor,
            final CrearAmbientePublisher crearPublisher,
            final ActualizarAmbientePublisher actualizarPublisher,
            final EliminarAmbientePublisher eliminarPublisher) {
        this.crearInteractor = crearInteractor;
        this.actualizarInteractor = actualizarInteractor;
        this.eliminarInteractor = eliminarInteractor;
        this.consultarInteractor = consultarInteractor;
        this.crearPublisher = crearPublisher;
        this.actualizarPublisher = actualizarPublisher;
        this.eliminarPublisher = eliminarPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Suscribirse a eventos de ambiente")
    public Flux<ServerSentEvent<AmbienteEvent>> publicarEventos() {
        final var eventos = Flux.merge(crearPublisher.getStream().cast(AmbienteEvent.class),
                actualizarPublisher.getStream().cast(AmbienteEvent.class),
                eliminarPublisher.getStream().cast(AmbienteEvent.class))
                .map(event -> ServerSentEvent.builder(event).event("ambiente").build());
        return Flux.concat(Mono.just(ServerSentEvent.<AmbienteEvent>builder()
                .comment("connected").build()), eventos);
    }

    @PostMapping
    @Operation(summary = "Crear ambientes")
    public Mono<ResponseEntity<AmbienteResponse>> crear(
            @RequestBody final CrearAmbienteDtoRequest request) {
        return Mono.fromCallable(() -> response(crearInteractor.execute(request),
                "Ambiente creado.", HttpStatus.CREATED))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ambientes")
    public Mono<ResponseEntity<AmbienteResponse>> actualizar(
            @PathVariable final UUID id, @RequestBody final ActualizarAmbienteDtoRequest request) {
        return Mono.fromCallable(() -> response(actualizarInteractor.execute(id, request),
                "Ambiente actualizado.", HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ambientes")
    public Mono<ResponseEntity<AmbienteResponse>> eliminar(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            eliminarInteractor.execute(id);
            return response(null, "Ambiente eliminado.", HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    @Operation(summary = "Consultar ambientes")
    public Mono<ResponseEntity<AmbienteResponse>> consultar(
            @RequestParam(defaultValue = "1") final int page,
            @RequestParam(defaultValue = "10") final int pageSize) {
        return Mono.fromCallable(() -> {
            var body = new AmbienteResponse();
            body.getAmbientes().addAll(consultarInteractor.execute(page, pageSize));
            return ResponseEntity.ok(body);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar por id")
    public Mono<ResponseEntity<AmbienteResponse>> consultarPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> response(consultarInteractor.execute(id), null, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private ResponseEntity<AmbienteResponse> response(final AmbienteEntity entity,
            final String message, final HttpStatus status) {
        var body = new AmbienteResponse();
        if (entity != null) {
            body.getAmbientes().add(entity);
        }
        if (message != null) {
            body.getMensajes().add(message);
        }
        return new ResponseEntity<>(body, status);
    }
}
