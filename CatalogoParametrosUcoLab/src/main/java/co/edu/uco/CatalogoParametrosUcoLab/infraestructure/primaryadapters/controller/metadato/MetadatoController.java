package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.metadato;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.consultarmetadato.primaryports.interactor.ConsultarMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto.ActualizarMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor.ActualizarMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.publisher.ActualizarMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.CrearMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.publisher.CrearMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.primaryports.interactor.EliminarMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.secondaryports.publisher.EliminarMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.secondaryports.event.MetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.metadato.MetadatoResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/metadatos")
@Tag(name = "Metadatos", description = "Administracion y consulta de los metadatos asociados a parametros.")
public final class MetadatoController {
    private final CrearMetadatoInteractor crear;
    private final EliminarMetadatoInteractor eliminar;
    private final ConsultarMetadatoInteractor consultar;
    private final CrearMetadatoPublisher crearPublisher;
    private final EliminarMetadatoPublisher eliminarPublisher;
    private final ActualizarMetadatoInteractor actualizar;
    private final ActualizarMetadatoPublisher actualizarPublisher;
    public MetadatoController(final CrearMetadatoInteractor crear, final EliminarMetadatoInteractor eliminar,
            final ConsultarMetadatoInteractor consultar, final CrearMetadatoPublisher crearPublisher,
            final EliminarMetadatoPublisher eliminarPublisher, final ActualizarMetadatoInteractor actualizar,
            final ActualizarMetadatoPublisher actualizarPublisher) {
        this.crear = crear; this.eliminar = eliminar; this.consultar = consultar;
        this.crearPublisher = crearPublisher; this.eliminarPublisher = eliminarPublisher;
        this.actualizar = actualizar; this.actualizarPublisher = actualizarPublisher;
    }
    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Suscribirse a eventos de metadatos", description = "Mantiene una conexion SSE abierta y publica cambios en los metadatos.")
    public Flux<ServerSentEvent<MetadatoEvent>> publicarEventos() {
        return Flux.merge(crearPublisher.getStream().cast(MetadatoEvent.class),
                actualizarPublisher.getStream().cast(MetadatoEvent.class), eliminarPublisher.getStream().cast(MetadatoEvent.class))
                .map(event -> ServerSentEvent.builder(event).event("metadato").build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un metadato", description = "Actualiza el valor y los datos del metadato indicado.")
    public Mono<ResponseEntity<MetadatoResponse>> actualizarMetadato(@PathVariable final UUID id,
            @RequestBody final ActualizarMetadatoDtoRequest request) {
        return Mono.fromCallable(() -> { actualizar.execute(id, request); var response = new MetadatoResponse();
            response.getMensajes().add("Metadato actualizado exitosamente."); return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }
    @PostMapping
    @Operation(summary = "Crear un metadato", description = "Registra un metadato para un parametro y un tipo de metadato.")
    public Mono<ResponseEntity<MetadatoResponse>> crearMetadato(@RequestBody final CrearMetadatoDtoRequest request) {
        return Mono.fromCallable(() -> { crear.execute(request); var response = new MetadatoResponse();
            response.getMensajes().add("Metadato creado exitosamente."); return new ResponseEntity<>(response, HttpStatus.CREATED);
        }).subscribeOn(Schedulers.boundedElastic());
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un metadato", description = "Elimina el metadato identificado por el UUID indicado.")
    public Mono<ResponseEntity<MetadatoResponse>> eliminarMetadato(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> { eliminar.execute(id); var response = new MetadatoResponse();
            response.getMensajes().add("Metadato eliminado exitosamente."); return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }
    @GetMapping
    @Operation(summary = "Consultar metadatos", description = "Obtiene todos los metadatos o los filtra por el identificador de un parametro.")
    public Mono<ResponseEntity<MetadatoResponse>> consultarMetadatos(@RequestParam(required = false) final UUID idParametro) {
        return Mono.fromCallable(() -> { var response = new MetadatoResponse(); response.getMetadatos().addAll(
                idParametro == null ? consultar.execute() : consultar.executeByParametro(idParametro));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Consultar un metadato", description = "Busca un metadato por su identificador UUID.")
    public Mono<ResponseEntity<MetadatoResponse>> consultarMetadato(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> { var response = new MetadatoResponse(); var data = consultar.execute(id);
            response.getMetadatos().addAll(data);
            if (data.isEmpty()) { response.getMensajes().add("No se encontro el metadato."); return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
