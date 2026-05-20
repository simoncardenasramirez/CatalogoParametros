package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.funcionalidad;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.ConsultarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.response.FuncionalidadResponse;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/funcionalidades")
public final class ConsultarFuncionalidadController {

    private final ConsultarFuncionalidadInteractor consultarFuncionalidadInteractor;

    public ConsultarFuncionalidadController(final ConsultarFuncionalidadInteractor consultarFuncionalidadInteractor) {
        this.consultarFuncionalidadInteractor = consultarFuncionalidadInteractor;
    }

    @GetMapping
    public ResponseEntity<FuncionalidadResponse> consultarTodos() {
        var response = new FuncionalidadResponse();

        try {
            var funcionalidades = consultarFuncionalidadInteractor.execute();
            response.getFuncionalidades().addAll(funcionalidades);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception exception) {
            response.getMensajes().add("Ocurrio un error consultando las funcionalidades.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionalidadResponse> consultarPorId(@PathVariable final UUID id) {
        var response = new FuncionalidadResponse();

        try {
            var funcionalidades = consultarFuncionalidadInteractor.execute(id);
            response.getFuncionalidades().addAll(funcionalidades);

            if (funcionalidades.isEmpty()) {
                response.getMensajes().add("No se encontro la funcionalidad con el id especificado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception exception) {
            response.getMensajes().add("Ocurrio un error consultando la funcionalidad.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
