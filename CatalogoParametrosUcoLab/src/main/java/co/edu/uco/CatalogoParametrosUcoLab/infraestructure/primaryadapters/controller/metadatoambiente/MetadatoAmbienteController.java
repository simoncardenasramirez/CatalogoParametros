package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.metadatoambiente;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.dto.ActualizarMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.interactor.ActualizarMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.consultarmetadatoambiente.primaryports.interactor.ConsultarMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto.CrearMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.interactor.CrearMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.primaryports.interactor.EliminarMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.publisher.CrearMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.publisher.ActualizarMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.secondaryports.publisher.EliminarMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.secondaryports.event.MetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.metadatoambiente.MetadatoAmbienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/metadatos-ambiente")
@Tag(name = "Metadatos de ambiente", description = "Relaciona parametros, ambientes y estados de metadato.")
public final class MetadatoAmbienteController {
    private final CrearMetadatoAmbienteInteractor crearInteractor;
    private final ActualizarMetadatoAmbienteInteractor actualizarInteractor;
    private final ConsultarMetadatoAmbienteInteractor consultarInteractor;
    private final EliminarMetadatoAmbienteInteractor eliminarInteractor;
    private final CrearMetadatoAmbientePublisher crearPublisher;
    private final ActualizarMetadatoAmbientePublisher actualizarPublisher;
    private final EliminarMetadatoAmbientePublisher eliminarPublisher;
    public MetadatoAmbienteController(final CrearMetadatoAmbienteInteractor crearInteractor,
            final ActualizarMetadatoAmbienteInteractor actualizarInteractor,
            final ConsultarMetadatoAmbienteInteractor consultarInteractor,
            final EliminarMetadatoAmbienteInteractor eliminarInteractor,
            final CrearMetadatoAmbientePublisher crearPublisher,
            final ActualizarMetadatoAmbientePublisher actualizarPublisher,
            final EliminarMetadatoAmbientePublisher eliminarPublisher) {
        this.crearInteractor = crearInteractor;
        this.actualizarInteractor = actualizarInteractor;
        this.consultarInteractor = consultarInteractor;
        this.eliminarInteractor = eliminarInteractor;
        this.crearPublisher = crearPublisher;
        this.actualizarPublisher = actualizarPublisher;
        this.eliminarPublisher = eliminarPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Suscribirse a eventos de metadato ambiente")
    public Flux<ServerSentEvent<MetadatoAmbienteEvent>> publicarEventos() {
        final var eventos = Flux.merge(crearPublisher.getStream().cast(MetadatoAmbienteEvent.class),
                actualizarPublisher.getStream().cast(MetadatoAmbienteEvent.class),
                eliminarPublisher.getStream().cast(MetadatoAmbienteEvent.class))
                .map(event -> ServerSentEvent.builder(event).event("metadatoambiente").build());
        return Flux.concat(Mono.just(ServerSentEvent.<MetadatoAmbienteEvent>builder()
                .comment("connected").build()), eventos);
    }
    @PostMapping @Operation(summary = "Crear metadato de ambiente")
    public Mono<ResponseEntity<MetadatoAmbienteResponse>> crear(
            @RequestBody final CrearMetadatoAmbienteDtoRequest request) {
        return Mono.fromCallable(() -> response(crearInteractor.execute(request), "Metadato creado.", HttpStatus.CREATED))
                .subscribeOn(Schedulers.boundedElastic());
    }
    @PutMapping("/{id}") @Operation(summary = "Actualizar metadato de ambiente")
    public Mono<ResponseEntity<MetadatoAmbienteResponse>> actualizar(@PathVariable final UUID id,
            @RequestBody final ActualizarMetadatoAmbienteDtoRequest request) {
        return Mono.fromCallable(() -> response(actualizarInteractor.execute(id, request), "Metadato actualizado.", HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }
    @DeleteMapping("/{id}") @Operation(summary = "Eliminar metadato de ambiente")
    public Mono<ResponseEntity<MetadatoAmbienteResponse>> eliminar(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> { eliminarInteractor.execute(id); return response(null, "Metadato eliminado.", HttpStatus.OK); })
                .subscribeOn(Schedulers.boundedElastic());
    }
    @GetMapping @Operation(summary = "Consultar metadatos de ambiente")
    public Mono<ResponseEntity<MetadatoAmbienteResponse>> consultar(
            @RequestParam(defaultValue = "1") final int page,
            @RequestParam(defaultValue = "10") final int pageSize) {
        return Mono.fromCallable(() -> {
            var body = new MetadatoAmbienteResponse();
            body.getMetadatosAmbiente().addAll(consultarInteractor.execute(page, pageSize));
            return ResponseEntity.ok(body);
        }).subscribeOn(Schedulers.boundedElastic());
    }
    @GetMapping("/{id}") @Operation(summary = "Consultar metadato de ambiente por id")
    public Mono<ResponseEntity<MetadatoAmbienteResponse>> consultarPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> response(consultarInteractor.execute(id), null, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }
    private ResponseEntity<MetadatoAmbienteResponse> response(final MetadatoAmbienteEntity entity,
            final String message, final HttpStatus status) {
        var body = new MetadatoAmbienteResponse();
        if (entity != null) body.getMetadatosAmbiente().add(entity);
        if (message != null) body.getMensajes().add(message);
        return new ResponseEntity<>(body, status);
    }
}
