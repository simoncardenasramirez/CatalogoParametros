package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.modulo;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.CrearModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.exception.ModuloException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametroResponse;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/modulos")
public final class ModuloController {

    private final CrearModuloInteractor crearModuloInteractor;

    public ModuloController(final CrearModuloInteractor crearModuloInteractor) {
        this.crearModuloInteractor = crearModuloInteractor;
    }

    @PostMapping
    public ResponseEntity<ParametroResponse> crear(@RequestBody final CrearModuloDto modulo) {
        var response = new ParametroResponse();

        try {
            crearModuloInteractor.execute(modulo);
            response.getMensajes().add("Modulo creado exitosamente.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (final ModuloException exception) {
            response.getMensajes().add(exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (final Exception exception) {
            response.getMensajes().add("Ocurrio un error creando el modulo: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
