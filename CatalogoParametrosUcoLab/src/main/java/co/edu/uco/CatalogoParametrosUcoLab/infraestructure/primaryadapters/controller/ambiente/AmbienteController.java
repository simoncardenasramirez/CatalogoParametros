package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.ambiente;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.consultarambiente.primaryports.interactor.ConsultarAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.ambiente.AmbienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/ambientes")
@Tag(name = "Ambientes", description = "Catalogo fijo de ambientes.")
public final class AmbienteController {
    private final ConsultarAmbienteInteractor consultarInteractor;

    public AmbienteController(final ConsultarAmbienteInteractor consultarInteractor) {
        this.consultarInteractor = consultarInteractor;
    }

    @GetMapping
    @Operation(summary = "Consultar ambientes")
    public Mono<ResponseEntity<AmbienteResponse>> consultar(
            @RequestParam(defaultValue = "1") final int page,
            @RequestParam(defaultValue = "10") final int pageSize) {
        return Mono.fromCallable(() -> {
            var body = new AmbienteResponse();
            body.getAmbientes().addAll(consultarInteractor.execute(page, pageSize));
            return ResponseEntity.ok(body);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar por id")
    public Mono<ResponseEntity<AmbienteResponse>> consultarPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> response(consultarInteractor.execute(id), null, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private ResponseEntity<AmbienteResponse> response(final AmbienteEntity entity,
            final String message, final HttpStatus status) {
        var body = new AmbienteResponse();
        if (entity != null) {
            body.getAmbientes().add(entity);
        }
        if (message != null) {
            body.getMensajes().add(message);
        }
        return new ResponseEntity<>(body, status);
    }
}
