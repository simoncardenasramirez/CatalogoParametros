package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.estadometadatoambiente;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.dto.ActualizarEstadoMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.interactor.ActualizarEstadoMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.consultarestadometadatoambiente.primaryports.interactor.ConsultarEstadoMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.dto.CrearEstadoMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.interactor.CrearEstadoMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.primaryports.interactor.EliminarEstadoMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.secondaryports.publisher.CrearEstadoMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.secondaryports.publisher.ActualizarEstadoMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.secondaryports.publisher.EliminarEstadoMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.secondaryports.event.EstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.CatalogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/estados-metadato-ambiente")
@Tag(name = "Estados de metadato ambiente", description = "Administracion de estados de metadato ambiente del catalogo.")
public final class EstadoMetadatoAmbienteController {
    private final CrearEstadoMetadatoAmbienteInteractor crearInteractor;
    private final ActualizarEstadoMetadatoAmbienteInteractor actualizarInteractor;
    private final EliminarEstadoMetadatoAmbienteInteractor eliminarInteractor;
    private final ConsultarEstadoMetadatoAmbienteInteractor consultarInteractor;
    private final CrearEstadoMetadatoAmbientePublisher crearPublisher;
    private final ActualizarEstadoMetadatoAmbientePublisher actualizarPublisher;
    private final EliminarEstadoMetadatoAmbientePublisher eliminarPublisher;

    public EstadoMetadatoAmbienteController(final CrearEstadoMetadatoAmbienteInteractor crearInteractor,
            final ActualizarEstadoMetadatoAmbienteInteractor actualizarInteractor,
            final EliminarEstadoMetadatoAmbienteInteractor eliminarInteractor,
            final ConsultarEstadoMetadatoAmbienteInteractor consultarInteractor,
            final CrearEstadoMetadatoAmbientePublisher crearPublisher,
            final ActualizarEstadoMetadatoAmbientePublisher actualizarPublisher,
            final EliminarEstadoMetadatoAmbientePublisher eliminarPublisher) {
        this.crearInteractor = crearInteractor;
        this.actualizarInteractor = actualizarInteractor;
        this.eliminarInteractor = eliminarInteractor;
        this.consultarInteractor = consultarInteractor;
        this.crearPublisher = crearPublisher;
        this.actualizarPublisher = actualizarPublisher;
        this.eliminarPublisher = eliminarPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Suscribirse a eventos de estadometadatoambiente")
    public Flux<ServerSentEvent<EstadoMetadatoAmbienteEvent>> publicarEventos() {
        final var eventos = Flux.merge(crearPublisher.getStream().cast(EstadoMetadatoAmbienteEvent.class),
                actualizarPublisher.getStream().cast(EstadoMetadatoAmbienteEvent.class),
                eliminarPublisher.getStream().cast(EstadoMetadatoAmbienteEvent.class))
                .map(event -> ServerSentEvent.builder(event).event("estadometadatoambiente").build());
        return Flux.concat(Mono.just(ServerSentEvent.<EstadoMetadatoAmbienteEvent>builder()
                .comment("connected").build()), eventos);
    }

    @PostMapping
    @Operation(summary = "Crear estado de metadato ambiente")
    public Mono<ResponseEntity<CatalogResponse<EstadoMetadatoAmbienteEntity>>> crear(
            @RequestBody final CrearEstadoMetadatoAmbienteDtoRequest request) {
        return Mono.fromCallable(() -> response(crearInteractor.execute(request),
                "Estados de metadato ambiente creado.", HttpStatus.CREATED))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estado de metadato ambiente")
    public Mono<ResponseEntity<CatalogResponse<EstadoMetadatoAmbienteEntity>>> actualizar(
            @PathVariable final UUID id, @RequestBody final ActualizarEstadoMetadatoAmbienteDtoRequest request) {
        return Mono.fromCallable(() -> response(actualizarInteractor.execute(id, request),
                "Estados de metadato ambiente actualizado.", HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estado de metadato ambiente")
    public Mono<ResponseEntity<CatalogResponse<EstadoMetadatoAmbienteEntity>>> eliminar(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            eliminarInteractor.execute(id);
            return response(null, "Estados de metadato ambiente eliminado.", HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    @Operation(summary = "Consultar estados de metadato ambiente")
    public Mono<ResponseEntity<CatalogResponse<EstadoMetadatoAmbienteEntity>>> consultar(
            @RequestParam(defaultValue = "1") final int page,
            @RequestParam(defaultValue = "10") final int pageSize) {
        return Mono.fromCallable(() -> {
            var body = new CatalogResponse<EstadoMetadatoAmbienteEntity>();
            body.getDatos().addAll(consultarInteractor.execute(page, pageSize));
            return ResponseEntity.ok(body);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar por id")
    public Mono<ResponseEntity<CatalogResponse<EstadoMetadatoAmbienteEntity>>> consultarPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> response(consultarInteractor.execute(id), null, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private ResponseEntity<CatalogResponse<EstadoMetadatoAmbienteEntity>> response(final EstadoMetadatoAmbienteEntity entity,
            final String message, final HttpStatus status) {
        var body = new CatalogResponse<EstadoMetadatoAmbienteEntity>();
        if (entity != null) {
            body.getDatos().add(entity);
        }
        if (message != null) {
            body.getMensajes().add(message);
        }
        return new ResponseEntity<>(body, status);
    }
}
