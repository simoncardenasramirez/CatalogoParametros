package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.parametro;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.ConsultarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametroResponse;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/parametros")
public final class ParametroController {

    private final CrearParametroInteractor crearParametroInteractor;
    private final ConsultarParametroInteractor consultarParametroInteractor;

    public ParametroController(final CrearParametroInteractor crearParametroInteractor,
            final ConsultarParametroInteractor consultarParametroInteractor) {
        this.crearParametroInteractor = crearParametroInteractor;
        this.consultarParametroInteractor = consultarParametroInteractor;
    }

    @PostMapping
    public ResponseEntity<ParametroResponse> crearParametro(@RequestBody final CrearParametroDto parametro) {
        var response = new ParametroResponse();

        try {
            crearParametroInteractor.execute(parametro);
            response.getMensajes().add("Parametro creado exitosamente.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (final ParametroException exception) {
            response.getMensajes().add(exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (final Exception exception) {
            response.getMensajes().add("Ocurrio un error creando el parametro.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ParametroResponse> consultarTodosLosParametros() {
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
    public ResponseEntity<ParametroResponse> consultarParametroPorId(@PathVariable final UUID id) {
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
