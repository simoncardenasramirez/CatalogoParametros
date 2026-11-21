package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.parametro;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosporidentidadservicio.primaryports.interactor.ConsultarParametrosPorIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.consumer.ServiceIdentityClientIdReader;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.token.ParametrosTokenResponse;
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
    public Mono<ResponseEntity<ParametrosTokenResponse>> consultar(final ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            final var resultado = consultar.execute(clientIdReader.read(exchange));
            final var response = new ParametrosTokenResponse(resultado.aplicacion(), resultado.ambiente(),
                    resultado.parametros());
            return ResponseEntity.ok(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
