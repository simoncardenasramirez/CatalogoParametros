package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.catalogoaplicacion;

import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.core.publisher.Flux;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.primaryports.interactor.ConsultarCambiosCatalogoAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.interactor.AdministrarCatalogoAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.dto.AdministrarCatalogoAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.catalogoaplicacion.CatalogoAplicacionResponse;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/aplicaciones/{idAplicacion}/administracion")
public final class CatalogoAplicacionController {
    private final AdministrarCatalogoAplicacionInteractor interactor;
    private final ConsultarCambiosCatalogoAplicacionInteractor cambios;
    public CatalogoAplicacionController(final AdministrarCatalogoAplicacionInteractor interactor,
            final ConsultarCambiosCatalogoAplicacionInteractor cambios) { this.interactor = interactor; this.cambios = cambios; }
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<CambioCatalogoAplicacion>> eventos(@PathVariable final UUID idAplicacion) {
        var eventos = cambios.execute(idAplicacion).map(evento -> ServerSentEvent.builder(evento).event("catalogo-aplicacion").build());
        var latidos = Flux.interval(java.time.Duration.ofSeconds(15))
                .map(numero -> ServerSentEvent.<CambioCatalogoAplicacion>builder().comment("keepalive").build());
        return eventos.mergeWith(latidos).subscribeOn(Schedulers.boundedElastic());
    }
    @GetMapping
    public Mono<ResponseEntity<CatalogoAplicacionResponse>> consultar(@PathVariable final UUID idAplicacion) {
        return Mono.fromCallable(() -> ResponseEntity.ok(new CatalogoAplicacionResponse(interactor.consultar(idAplicacion))))
                .subscribeOn(Schedulers.boundedElastic());
    }
    @PostMapping("/{recurso}")
    public Mono<ResponseEntity<Response>> crear(@PathVariable final UUID idAplicacion, @PathVariable final String recurso,
            @RequestBody final AdministrarCatalogoAplicacionDtoRequest datos) {
        return respuesta(() -> { interactor.crear(idAplicacion, recurso, datos); return mensaje("Registro creado."); }, HttpStatus.CREATED);
    }
    @PutMapping("/{recurso}/{id}")
    public Mono<ResponseEntity<Response>> actualizar(@PathVariable final UUID idAplicacion, @PathVariable final String recurso,
            @PathVariable final UUID id, @RequestBody final AdministrarCatalogoAplicacionDtoRequest datos) {
        return respuesta(() -> { interactor.actualizar(idAplicacion, recurso, id, datos); return mensaje("Registro actualizado."); }, HttpStatus.OK);
    }
    @DeleteMapping("/{recurso}/{id}")
    public Mono<ResponseEntity<Response>> eliminar(@PathVariable final UUID idAplicacion, @PathVariable final String recurso,
            @PathVariable final UUID id) {
        return respuesta(() -> { interactor.eliminar(idAplicacion, recurso, id); return mensaje("Registro eliminado."); }, HttpStatus.OK);
    }
    private Response mensaje(final String mensaje) { var response = new Response(); response.getMensajes().add(mensaje); return response; }
    private Mono<ResponseEntity<Response>> respuesta(final Supplier<Response> accion, final HttpStatus status) {
        return Mono.fromCallable(() -> new ResponseEntity<>(accion.get(), status)).subscribeOn(Schedulers.boundedElastic());
    }
}
