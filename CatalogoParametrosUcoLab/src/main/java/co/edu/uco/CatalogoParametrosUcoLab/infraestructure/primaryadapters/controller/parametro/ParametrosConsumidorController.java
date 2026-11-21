package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.parametro;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporidentidadservicio.primaryports.interactor.ConsultarParametrosPorIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.consumer.ServiceIdentityClientIdReader;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametrosConsumidorResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/** Consulta de parámetros para backends autenticados con identidad de servicio. */
@RestController
@RequestMapping("/catalogo-parametros/api/v1/parametros")
public final class ParametrosConsumidorController {
    private final ConsultarParametrosPorIdentidadServicioInteractor consultar;
    private final ServiceIdentityClientIdReader clientIdReader;

    public ParametrosConsumidorController(final ConsultarParametrosPorIdentidadServicioInteractor consultar,
            final ServiceIdentityClientIdReader clientIdReader) {
        this.consultar = consultar;
        this.clientIdReader = clientIdReader;
    }

    @GetMapping("/consulta/m2m")
    public Mono<ResponseEntity<ParametrosConsumidorResponse>> consultar(final ServerWebExchange exchange,
            @RequestParam(name = "nombre", required = false) final String nombre) {
        return Mono.fromCallable(() -> {
            final var resultado = consultar.execute(clientIdReader.read(exchange), nombre);
            final var response = new ParametrosConsumidorResponse(resultado.aplicacion(), resultado.ambiente(),
                    resultado.parametros());
            return ResponseEntity.ok(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
