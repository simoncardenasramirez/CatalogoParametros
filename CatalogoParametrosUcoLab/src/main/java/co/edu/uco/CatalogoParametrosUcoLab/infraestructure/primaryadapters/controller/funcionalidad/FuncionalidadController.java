package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.funcionalidad;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.dto.CambiarEstadoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.interactor.CambiarEstadoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.interactor.CambiarEstadoInteractor.TipoRecurso;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.ActualizarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.consultarfuncionalidad.primaryports.interactor.ConsultarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.CrearFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.publisher.CrearFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.primaryports.interactor.EliminarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.publisher.EliminarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.BusinessException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.secondaryports.event.FuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.funcionalidad.FuncionalidadResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametroResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/funcionalidades")
@Tag(name = "Funcionalidades", description = "Administracion y consulta de las funcionalidades de los modulos.")
public final class FuncionalidadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionalidadController.class);

    private final CrearFuncionalidadInteractor crearFuncionalidadInteractor;
    private final ActualizarFuncionalidadInteractor actualizarFuncionalidadInteractor;
    private final EliminarFuncionalidadInteractor eliminarFuncionalidadInteractor;
    private final ConsultarFuncionalidadInteractor consultarFuncionalidadInteractor;
    private final CrearFuncionalidadPublisher crearFuncionalidadPublisher;
    private final ActualizarFuncionalidadPublisher actualizarFuncionalidadPublisher;
    private final EliminarFuncionalidadPublisher eliminarFuncionalidadPublisher;
    private final CambiarEstadoInteractor cambiarEstadoInteractor;

    public FuncionalidadController(final CrearFuncionalidadInteractor crearFuncionalidadInteractor,
            final ActualizarFuncionalidadInteractor actualizarFuncionalidadInteractor,
            final EliminarFuncionalidadInteractor eliminarFuncionalidadInteractor,
            final ConsultarFuncionalidadInteractor consultarFuncionalidadInteractor,
            final CrearFuncionalidadPublisher crearFuncionalidadPublisher,
            final ActualizarFuncionalidadPublisher actualizarFuncionalidadPublisher,
            final EliminarFuncionalidadPublisher eliminarFuncionalidadPublisher,
            final CambiarEstadoInteractor cambiarEstadoInteractor) {
        this.crearFuncionalidadInteractor = crearFuncionalidadInteractor;
        this.actualizarFuncionalidadInteractor = actualizarFuncionalidadInteractor;
        this.eliminarFuncionalidadInteractor = eliminarFuncionalidadInteractor;
        this.consultarFuncionalidadInteractor = consultarFuncionalidadInteractor;
        this.crearFuncionalidadPublisher = crearFuncionalidadPublisher;
        this.actualizarFuncionalidadPublisher = actualizarFuncionalidadPublisher;
        this.eliminarFuncionalidadPublisher = eliminarFuncionalidadPublisher;
        this.cambiarEstadoInteractor = cambiarEstadoInteractor;
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Suscribirse a eventos de funcionalidades", description = "Mantiene una conexion SSE abierta y publica cambios en las funcionalidades.")
    public Flux<ServerSentEvent<FuncionalidadEvent>> publicarEventos() {
        var eventos = Flux.merge(crearFuncionalidadPublisher.getStream().cast(FuncionalidadEvent.class),
                actualizarFuncionalidadPublisher.getStream().cast(FuncionalidadEvent.class),
                eliminarFuncionalidadPublisher.getStream().cast(FuncionalidadEvent.class))
                .map(event -> ServerSentEvent.builder(event)
                        .event("funcionalidad")
                        .build());

        return Flux.concat(Mono.just(ServerSentEvent.<FuncionalidadEvent>builder()
                .comment("connected")
                .build()), eventos);
    }

    @PostMapping
    @Operation(summary = "Crear una funcionalidad", description = "Registra una nueva funcionalidad asociada a un modulo.")
    public Mono<ResponseEntity<ParametroResponse>> crearFuncionalidad(@RequestBody final CrearFuncionalidadDtoRequest funcionalidad) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                crearFuncionalidadInteractor.execute(funcionalidad);
                response.getMensajes().add("Funcionalidad creada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error creando la funcionalidad: " + exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una funcionalidad", description = "Actualiza los datos de una funcionalidad existente.")
    public Mono<ResponseEntity<ParametroResponse>> actualizarFuncionalidad(@PathVariable final UUID id,
            @RequestBody final ActualizarFuncionalidadDtoRequest funcionalidad) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                actualizarFuncionalidadInteractor.execute(id, funcionalidad);
                response.getMensajes().add("Funcionalidad actualizada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error actualizando la funcionalidad.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping("/{id}/cambiarestado")
    @Operation(summary = "Cambiar el estado de una funcionalidad", description = "Activa o desactiva la funcionalidad indicada.")
    public Mono<ResponseEntity<ParametroResponse>> cambiarEstado(@PathVariable final UUID id,
            @RequestBody final CambiarEstadoDtoRequest request) {
        return Mono.fromCallable(() -> {
            cambiarEstadoInteractor.execute(TipoRecurso.FUNCIONALIDAD, id, request.getActivo());
            var response = new ParametroResponse();
            response.getMensajes().add("Estado de la funcionalidad actualizado exitosamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una funcionalidad", description = "Elimina una funcionalidad cuando no esta siendo utilizada por parametros.")
    public Mono<ResponseEntity<ParametroResponse>> eliminarFuncionalidad(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new ParametroResponse();

            try {
                eliminarFuncionalidadInteractor.execute(id);
                response.getMensajes().add("Funcionalidad eliminada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final BusinessException exception) {
                throw exception;
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error eliminando la funcionalidad.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    @Operation(summary = "Consultar funcionalidades", description = "Obtiene las funcionalidades de forma paginada.")
    public Mono<ResponseEntity<FuncionalidadResponse>> consultarTodasLasFuncionalidades(
            @RequestParam(defaultValue = "1") final int page, @RequestParam(defaultValue = "10") final int pageSize) {
        return Mono.fromCallable(() -> {
            var response = new FuncionalidadResponse();

            try {
                var funcionalidades = consultarFuncionalidadInteractor.execute(page, pageSize);
                response.getFuncionalidades().addAll(funcionalidades);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                LOGGER.error("Error consultando las funcionalidades", exception);
                response.getMensajes().add("Ocurrio un error consultando las funcionalidades.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una funcionalidad", description = "Busca una funcionalidad por su identificador UUID.")
    public Mono<ResponseEntity<FuncionalidadResponse>> consultarFuncionalidadesPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new FuncionalidadResponse();

            try {
                var funcionalidades = consultarFuncionalidadInteractor.execute(id);
                response.getFuncionalidades().addAll(funcionalidades);

                if (funcionalidades.isEmpty()) {
                    response.getMensajes().add("No se encontro la funcionalidad con el id especificado.");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando la funcionalidad.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
