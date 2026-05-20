package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.funcionalidad;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.funcionalidad.CrearFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.funcionalidad.CrearFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.domain.funcionalidad.exception.FuncionalidadException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.response.ParametroResponse;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/funcionalidades")
public final class CrearFuncionalidadController {

    private final CrearFuncionalidadInteractor crearFuncionalidadInteractor;

    public CrearFuncionalidadController(final CrearFuncionalidadInteractor crearFuncionalidadInteractor) {
        this.crearFuncionalidadInteractor = crearFuncionalidadInteractor;
    }

    @PostMapping
    public ResponseEntity<ParametroResponse> crear(@RequestBody final CrearFuncionalidadDto funcionalidad) {
        var response = new ParametroResponse();

        try {
            crearFuncionalidadInteractor.execute(funcionalidad);
            response.getMensajes().add("Funcionalidad creada exitosamente.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (final FuncionalidadException exception) {
            response.getMensajes().add(exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (final Exception exception) {
            response.getMensajes().add("Ocurrio un error creando la funcionalidad: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}