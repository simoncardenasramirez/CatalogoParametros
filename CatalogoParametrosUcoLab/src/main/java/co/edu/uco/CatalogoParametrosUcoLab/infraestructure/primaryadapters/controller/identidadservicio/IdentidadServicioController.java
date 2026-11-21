package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.identidadservicio;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.consultaridentidadservicio.primaryports.interactor.ConsultarIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.eliminaridentidadservicio.primaryports.interactor.EliminarIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto.RegistrarIdentidadServicioDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.interactor.RegistrarIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.BusinessException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.identidadservicio.IdentidadServicioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/aplicaciones/{idAplicacion}/identidades-servicio")
@Tag(name = "Identidades de servicio", description = "Asocia client_id tecnicos a aplicaciones y ambientes.")
public final class IdentidadServicioController {
    private final RegistrarIdentidadServicioInteractor registrar;
    private final ConsultarIdentidadServicioInteractor consultar;
    private final EliminarIdentidadServicioInteractor eliminar;

    public IdentidadServicioController(final RegistrarIdentidadServicioInteractor registrar,
            final ConsultarIdentidadServicioInteractor consultar,
            final EliminarIdentidadServicioInteractor eliminar) {
        this.registrar = registrar;
        this.consultar = consultar;
        this.eliminar = eliminar;
    }

    @GetMapping
    @Operation(summary = "Consultar identidades de servicio de una aplicacion")
    public Mono<ResponseEntity<IdentidadServicioResponse>> consultar(@PathVariable final UUID idAplicacion) {
        return Mono.fromCallable(() -> {
            final var response = new IdentidadServicioResponse();
            response.getIdentidadesServicio().addAll(consultar.execute(idAplicacion));
            return ResponseEntity.ok(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping
    @Operation(summary = "Registrar identidad de servicio para un ambiente")
    public Mono<ResponseEntity<IdentidadServicioResponse>> registrar(@PathVariable final UUID idAplicacion,
            @RequestBody final RegistrarIdentidadServicioDtoRequest request) {
        return Mono.fromCallable(() -> {
            final var response = new IdentidadServicioResponse();
            try {
                response.getIdentidadesServicio().add(registrar.execute(idAplicacion, request));
                response.getMensajes().add("Identidad de servicio registrada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (final BusinessException exception) {
                throw exception;
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{clientId}")
    @Operation(summary = "Retirar identidad de servicio")
    public Mono<ResponseEntity<IdentidadServicioResponse>> eliminar(@PathVariable final UUID idAplicacion,
            @PathVariable final String clientId) {
        return Mono.fromCallable(() -> {
            eliminar.execute(idAplicacion, clientId);
            final var response = new IdentidadServicioResponse();
            response.getMensajes().add("Identidad de servicio retirada exitosamente.");
            return ResponseEntity.ok(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
