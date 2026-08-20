package co.edu.uco.CatalogoParametrosUcoLab.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

class UseCaseWithOutReturnTest {

    @Test
    void debeEjecutarLaOperacionCuandoLosDatosSonValidos() {
        var ejecutado = new AtomicBoolean(false);
        UseCaseWithOutReturn<String> useCase = data -> ejecutado.set(true);

        assertDoesNotThrow(() -> useCase.execute("dato"));
        assertEquals(true, ejecutado.get());
    }

    @Test
    void debePropagarLaExcepcionCuandoLaOperacionFalla() {
        UseCaseWithOutReturn<String> useCase = data -> {
            throw TechnicalException.build("Error tecnico.");
        };

        assertThrows(TechnicalException.class, () -> useCase.execute("dato"));
    }
}