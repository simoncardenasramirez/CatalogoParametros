package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.mensaje;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.message.ConsultarMensajeDetalleInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.mensaje.MensajeResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/mensajes")
public final class MensajeController {

    private final ConsultarMensajeDetalleInteractor consultarMensajeDetalleInteractor;

    public MensajeController(final ConsultarMensajeDetalleInteractor consultarMensajeDetalleInteractor) {
        this.consultarMensajeDetalleInteractor = consultarMensajeDetalleInteractor;
    }

    @GetMapping("/{codigo}")
    public Mono<ResponseEntity<MensajeResponse>> consultarMensaje(@PathVariable final String codigo) {
        return Mono.fromCallable(() -> {
            final MensajeDetalle mensaje = consultarMensajeDetalleInteractor.execute(codigo);
            final MensajeResponse response = new MensajeResponse(
                    mensaje.code(),
                    mensaje.title(),
                    mensaje.content(),
                    mensaje.type(),
                    mensaje.category(),
                    mensaje.functionality());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
