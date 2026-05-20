package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.parametro;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.parametro.CrearParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.parametro.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.response.ParametroResponse;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/parametros")
public final class CrearParametroController {

    private final CrearParametroInteractor crearParametroInteractor;

    public CrearParametroController(final CrearParametroInteractor crearParametroInteractor) {
        this.crearParametroInteractor = crearParametroInteractor;
    }

    @PostMapping
    public ResponseEntity<ParametroResponse> crear(@RequestBody final CrearParametroDto parametro) {
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
}
