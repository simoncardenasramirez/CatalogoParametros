package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.tipometadato;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.tipometadato.consultartipometadato.primaryports.interactor.ConsultarTipoMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.tipometadato.TipoMetadatoResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/tipos-metadato")
@Tag(name = "Tipos de metadato", description = "Consulta de los tipos de metadato disponibles.")
public final class TipoMetadatoController {
    private final ConsultarTipoMetadatoInteractor consultarTipoMetadatoInteractor;

    public TipoMetadatoController(final ConsultarTipoMetadatoInteractor consultarTipoMetadatoInteractor) {
        this.consultarTipoMetadatoInteractor = consultarTipoMetadatoInteractor;
    }

    @GetMapping
    @Operation(summary = "Consultar tipos de metadato", description = "Obtiene todos los tipos de metadato disponibles en el catalogo.")
    public Mono<ResponseEntity<TipoMetadatoResponse>> consultarTodosLosTiposMetadato() {
        return Mono.fromCallable(() -> {
            var response = new TipoMetadatoResponse();
            response.getTiposMetadato().addAll(consultarTipoMetadatoInteractor.execute());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un tipo de metadato", description = "Busca un tipo de metadato por su identificador UUID.")
    public Mono<ResponseEntity<TipoMetadatoResponse>> consultarTipoMetadatoPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new TipoMetadatoResponse();
            var tipos = consultarTipoMetadatoInteractor.execute(id);
            response.getTiposMetadato().addAll(tipos);
            if (tipos.isEmpty()) {
                response.getMensajes().add("No se encontro el tipo de metadato con el id especificado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
