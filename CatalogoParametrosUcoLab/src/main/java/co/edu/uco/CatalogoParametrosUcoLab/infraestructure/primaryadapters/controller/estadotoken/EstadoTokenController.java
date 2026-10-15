package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.estadotoken;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadotoken.consultarestadotoken.primaryports.interactor.ConsultarEstadoTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.estadotoken.EstadoTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/estados-token")
@Tag(name = "Estados de token", description = "Consulta de los estados de token disponibles.")
public final class EstadoTokenController {
    private final ConsultarEstadoTokenInteractor consultarEstadoTokenInteractor;

    public EstadoTokenController(final ConsultarEstadoTokenInteractor consultarEstadoTokenInteractor) {
        this.consultarEstadoTokenInteractor = consultarEstadoTokenInteractor;
    }

    @GetMapping
    @Operation(summary = "Consultar estados de token",
            description = "Obtiene todos los estados de token disponibles en el catalogo.")
    public Mono<ResponseEntity<EstadoTokenResponse>> consultarTodosLosEstadosToken() {
        return Mono.fromCallable(() -> {
            var response = new EstadoTokenResponse();
            response.getEstadosToken().addAll(consultarEstadoTokenInteractor.execute());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un estado de token",
            description = "Busca un estado de token por su identificador UUID.")
    public Mono<ResponseEntity<EstadoTokenResponse>> consultarEstadoTokenPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new EstadoTokenResponse();
            var estados = consultarEstadoTokenInteractor.execute(id);
            response.getEstadosToken().addAll(estados);
            if (estados.isEmpty()) {
                response.getMensajes().add("No se encontro el estado de token con el id especificado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
