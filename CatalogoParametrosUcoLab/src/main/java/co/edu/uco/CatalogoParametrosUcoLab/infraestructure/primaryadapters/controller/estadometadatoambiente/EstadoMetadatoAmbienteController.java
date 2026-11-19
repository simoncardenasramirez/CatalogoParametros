package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.estadometadatoambiente;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.consultarestadometadatoambiente.primaryports.interactor.ConsultarEstadoMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.estadometadatoambiente.EstadoMetadatoAmbienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/estados-metadato-ambiente")
@Tag(name = "Estados de metadato ambiente", description = "Consulta del catalogo fijo de estados de metadato ambiente.")
public final class EstadoMetadatoAmbienteController {
    private final ConsultarEstadoMetadatoAmbienteInteractor consultarInteractor;

    public EstadoMetadatoAmbienteController(final ConsultarEstadoMetadatoAmbienteInteractor consultarInteractor) {
        this.consultarInteractor = consultarInteractor;
    }

    @GetMapping
    @Operation(summary = "Consultar estados de metadato ambiente")
    public Mono<ResponseEntity<EstadoMetadatoAmbienteResponse>> consultar(
            @RequestParam(defaultValue = "1") final int page,
            @RequestParam(defaultValue = "10") final int pageSize) {
        return Mono.fromCallable(() -> {
            var body = new EstadoMetadatoAmbienteResponse();
            body.getEstadosMetadatoAmbiente().addAll(consultarInteractor.execute(page, pageSize));
            return ResponseEntity.ok(body);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar por id")
    public Mono<ResponseEntity<EstadoMetadatoAmbienteResponse>> consultarPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> response(consultarInteractor.execute(id), null, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private ResponseEntity<EstadoMetadatoAmbienteResponse> response(final EstadoMetadatoAmbienteEntity entity,
            final String message, final HttpStatus status) {
        var body = new EstadoMetadatoAmbienteResponse();
        if (entity != null) {
            body.getEstadosMetadatoAmbiente().add(entity);
        }
        if (message != null) {
            body.getMensajes().add(message);
        }
        return new ResponseEntity<>(body, status);
    }
}
