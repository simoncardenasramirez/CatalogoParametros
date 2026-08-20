package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.EliminarAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class EliminarAplicacionInteractorImplTest {

    @Mock
    private EliminarAplicacion eliminarAplicacion;

    @InjectMocks
    private EliminarAplicacionInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseCuandoElIdEsValido() {
        var id = UUID.randomUUID();

        interactor.execute(id);

        verify(eliminarAplicacion).execute(id);
    }

    @Test
    void debeRelanzarLaExcepcionCuandoElUseCaseFalla() {
        var id = UUID.randomUUID();
        org.mockito.Mockito.doThrow(ValidationException.build("error"))
                .when(eliminarAplicacion).execute(id);

        assertThrows(ValidationException.class, () -> interactor.execute(id));
    }
}