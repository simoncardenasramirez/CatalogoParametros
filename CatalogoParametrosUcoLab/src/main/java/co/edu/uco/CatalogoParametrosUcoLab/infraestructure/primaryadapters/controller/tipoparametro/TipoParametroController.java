package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.tipoparametro;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.tipoparametro.consultartipoparametro.primaryports.interactor.ConsultarTipoParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.tipoparametro.TipoParametroResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/tipos-parametro")
public final class TipoParametroController {

    private final ConsultarTipoParametroInteractor consultarTipoParametroInteractor;

    public TipoParametroController(final ConsultarTipoParametroInteractor consultarTipoParametroInteractor) {
        this.consultarTipoParametroInteractor = consultarTipoParametroInteractor;
    }

    @GetMapping
    public Mono<ResponseEntity<TipoParametroResponse>> consultarTodosLosTiposParametro() {
        return Mono.fromCallable(() -> {
            var response = new TipoParametroResponse();

            try {
                var tiposParametro = consultarTipoParametroInteractor.execute();
                response.getTiposParametro().addAll(tiposParametro);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando los tipos de parametro.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TipoParametroResponse>> consultarTipoParametroPorId(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new TipoParametroResponse();

            try {
                var tiposParametro = consultarTipoParametroInteractor.execute(id);
                response.getTiposParametro().addAll(tiposParametro);

                if (tiposParametro.isEmpty()) {
                    response.getMensajes().add("No se encontro el tipo de parametro con el id especificado.");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (final Exception exception) {
                response.getMensajes().add("Ocurrio un error consultando el tipo de parametro.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
