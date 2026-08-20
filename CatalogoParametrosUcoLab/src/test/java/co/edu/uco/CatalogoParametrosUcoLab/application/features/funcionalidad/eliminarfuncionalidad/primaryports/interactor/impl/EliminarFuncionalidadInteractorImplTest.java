package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.EliminarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class EliminarFuncionalidadInteractorImplTest {

    @Mock
    private EliminarFuncionalidad eliminarFuncionalidad;

    @InjectMocks
    private EliminarFuncionalidadInteractorImpl interactor;

    @Test
    void debeDelegarEnElCasoDeUsoCuandoSeEjecuta() {
        var id = UUID.randomUUID();

        interactor.execute(id);

        verify(eliminarFuncionalidad).execute(id);
    }

    @Test
    void debeRelanzarLaExcepcionCuandoElCasoDeUsoFalla() {
        var id = UUID.randomUUID();
        org.mockito.Mockito.doThrow(NotFoundException.build("no existe la funcionalidad"))
                .when(eliminarFuncionalidad).execute(id);

        assertThrows(NotFoundException.class, () -> interactor.execute(id));
    }
}