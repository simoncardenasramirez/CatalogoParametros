package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.modulo;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto.ActualizarModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.ActualizarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.event.ActualizarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.consultarmodulo.primaryports.interactor.ConsultarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.CrearModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.event.CrearModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.publisher.CrearModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.BusinessException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.secondaryports.event.ModuloEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.modulo.ModuloResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametroResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/modulos")
public final class ModuloController {

    private final CrearModuloInteractor crearModuloInteractor;
    private final ConsultarModuloInteractor consultarModuloInteractor;
    private final CrearModuloPublisher crearModuloPublisher;
    private final ActualizarModuloInteractor actualizarModuloInteractor;
    private final ActualizarModuloPublisher actualizarModuloPublisher;

    public ModuloController(final CrearModuloInteractor crearModuloInteractor,
            final ConsultarModuloInteractor consultarModuloInteractor,
            final CrearModuloPublisher crearModuloPublisher,
            final ActualizarModuloInteractor actualizarModuloInteractor,
            final ActualizarModuloPublisher actualizarModuloPublisher) {
        this.crearModuloInteractor = crearModuloInteractor;
        this.consultarModuloInteractor = consultarModuloInteractor;
        this.crearModuloPublisher = crearModuloPublisher;
        this.actualizarModuloInteractor = actualizarModuloInteractor;
        this.actualizarModuloPublisher = actualizarModuloPublisher;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ModuloEvent>> publicarEventos() {
        var eventosCrear = crearModuloPublisher.getStream().cast(ModuloEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("modulo")
                        .build());

        var eventosActualizar = actualizarModuloPublisher.getStream().cast(ModuloEvent.class)
                .map(event -> ServerSentEvent.builder(event)
                        .event("modulo")
                        .build());

        return Flux.concat(Mono.just(ServerSentEvent.<ModuloEvent>builder()
                .comment("connected")
                .build()), Flux.merge(eventosCrear, eventosActualizar));
    }

    @PostMapping
    public Mono<ResponseEntity<ParametroResponse>> crear(@RequestBody final CrearModuloDtoRequest modulo) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                crearModuloInteractor.execute(modulo);
                response.getMensajes().add("Modulo creado exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error creando el modulo: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ParametroResponse>> actualizar(@PathVariable final UUID id,
            @RequestBody final ActualizarModuloDtoRequest modulo) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                actualizarModuloInteractor.execute(id, modulo);
                response.getMensajes().add("Modulo actualizado exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error actualizando el modulo: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    public Mono<ResponseEntity<ModuloResponse>> consultarTodosLosModulos() {
        return Mono.fromCallable(() -> {
            var response = new ModuloResponse();

            try {
                var modulos = consultarModuloInteractor.execute();
                response.getModulos().addAll(modulos);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando los modulos.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ModuloResponse>> consultarModulosPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new ModuloResponse();

            try {
                var modulos = consultarModuloInteractor.execute(id);
                response.getModulos().addAll(modulos);

                if (modulos.isEmpty()) {
                    response.getMensajes().add("No se encontro el modulo con el id especificado.");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando el modulo.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
