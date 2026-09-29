package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.estadoambiente;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.dto.ActualizarEstadoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.interactor.ActualizarEstadoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.consultarestadoambiente.primaryports.interactor.ConsultarEstadoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.dto.CrearEstadoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.interactor.CrearEstadoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.primaryports.interactor.EliminarEstadoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.secondaryports.publisher.CrearEstadoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.secondaryports.publisher.ActualizarEstadoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.secondaryports.publisher.EliminarEstadoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.secondaryports.event.EstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.estadoambiente.EstadoAmbienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/estados-ambiente")
@Tag(name = "Estados de ambiente", description = "Administracion de estados de ambiente del catalogo.")
public final class EstadoAmbienteController {
    private final CrearEstadoAmbienteInteractor crearInteractor;
    private final ActualizarEstadoAmbienteInteractor actualizarInteractor;
    private final EliminarEstadoAmbienteInteractor eliminarInteractor;
    private final ConsultarEstadoAmbienteInteractor consultarInteractor;
    private final CrearEstadoAmbientePublisher crearPublisher;
    private final ActualizarEstadoAmbientePublisher actualizarPublisher;
    private final EliminarEstadoAmbientePublisher eliminarPublisher;

    public EstadoAmbienteController(final CrearEstadoAmbienteInteractor crearInteractor,
            final ActualizarEstadoAmbienteInteractor actualizarInteractor,
            final EliminarEstadoAmbienteInteractor eliminarInteractor,
            final ConsultarEstadoAmbienteInteractor consultarInteractor,
            final CrearEstadoAmbientePublisher crearPublisher,
            final ActualizarEstadoAmbientePublisher actualizarPublisher,
            final EliminarEstadoAmbientePublisher eliminarPublisher) {
        this.crearInteractor = crearInteractor;
        this.actualizarInteractor = actualizarInteractor;
        this.eliminarInteractor = eliminarInteractor;
        this.consultarInteractor = consultarInteractor;
        this.crearPublisher = crearPublisher;
        this.actualizarPublisher = actualizarPublisher;
        this.eliminarPublisher = eliminarPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Suscribirse a eventos de estadoambiente")
    public Flux<ServerSentEvent<EstadoAmbienteEvent>> publicarEventos() {
        final var eventos = Flux.merge(crearPublisher.getStream().cast(EstadoAmbienteEvent.class),
                actualizarPublisher.getStream().cast(EstadoAmbienteEvent.class),
                eliminarPublisher.getStream().cast(EstadoAmbienteEvent.class))
                .map(event -> ServerSentEvent.builder(event).event("estadoambiente").build());
        return Flux.concat(Mono.just(ServerSentEvent.<EstadoAmbienteEvent>builder()
                .comment("connected").build()), eventos);
    }

    @PostMapping
    @Operation(summary = "Crear estado de ambiente")
    public Mono<ResponseEntity<EstadoAmbienteResponse>> crear(
            @RequestBody final CrearEstadoAmbienteDtoRequest request) {
        return Mono.fromCallable(() -> response(crearInteractor.execute(request),
                "Estados de ambiente creado.", HttpStatus.CREATED))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estado de ambiente")
    public Mono<ResponseEntity<EstadoAmbienteResponse>> actualizar(
            @PathVariable final UUID id, @RequestBody final ActualizarEstadoAmbienteDtoRequest request) {
        return Mono.fromCallable(() -> response(actualizarInteractor.execute(id, request),
                "Estados de ambiente actualizado.", HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estado de ambiente")
    public Mono<ResponseEntity<EstadoAmbienteResponse>> eliminar(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            eliminarInteractor.execute(id);
            return response(null, "Estados de ambiente eliminado.", HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    @Operation(summary = "Consultar estados de ambiente")
    public Mono<ResponseEntity<EstadoAmbienteResponse>> consultar(
            @RequestParam(defaultValue = "1") final int page,
            @RequestParam(defaultValue = "10") final int pageSize) {
        return Mono.fromCallable(() -> {
            var body = new EstadoAmbienteResponse();
            body.getEstadosAmbiente().addAll(consultarInteractor.execute(page, pageSize));
            return ResponseEntity.ok(body);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar por id")
    public Mono<ResponseEntity<EstadoAmbienteResponse>> consultarPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> response(consultarInteractor.execute(id), null, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private ResponseEntity<EstadoAmbienteResponse> response(final EstadoAmbienteEntity entity,
            final String message, final HttpStatus status) {
        var body = new EstadoAmbienteResponse();
        if (entity != null) {
            body.getEstadosAmbiente().add(entity);
        }
        if (message != null) {
            body.getMensajes().add(message);
        }
        return new ResponseEntity<>(body, status);
    }
}
