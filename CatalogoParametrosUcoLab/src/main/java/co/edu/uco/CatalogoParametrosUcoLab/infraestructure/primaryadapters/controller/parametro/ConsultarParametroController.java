package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.parametro;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.parametro.ConsultarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.response.ParametroResponse;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/parametros")
public final class ConsultarParametroController {

    private final ConsultarParametroInteractor consultarParametroInteractor;

    public ConsultarParametroController(final ConsultarParametroInteractor consultarParametroInteractor) {
        this.consultarParametroInteractor = consultarParametroInteractor;
    }

    @GetMapping
    public ResponseEntity<ParametroResponse> consultarTodos() {
        var response = new ParametroResponse();

        try {
            var parametros = consultarParametroInteractor.execute();
            response.getParametros().addAll(parametros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception exception) {
            response.getMensajes().add("Ocurrio un error consultando los parametros.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParametroResponse> consultarPorId(@PathVariable final UUID id) {
        var response = new ParametroResponse();

        try {
            var parametros = consultarParametroInteractor.execute(id);
            response.getParametros().addAll(parametros);
            
            if (parametros.isEmpty()) {
                response.getMensajes().add("No se encontro el parametro con el id especificado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception exception) {
            response.getMensajes().add("Ocurrio un error consultando el parametro.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}