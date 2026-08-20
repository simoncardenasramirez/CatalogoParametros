package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.EliminarOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class EliminarOrganizacionInteractorImplTest {

    @Mock
    private EliminarOrganizacion eliminarOrganizacion;

    @InjectMocks
    private EliminarOrganizacionInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseConElId() {
        var id = UUID.randomUUID();

        interactor.execute(id);

        verify(eliminarOrganizacion).execute(id);
    }

    @Test
    void debeRelanzarLaExcepcionCuandoElUseCaseFalla() {
        var id = UUID.randomUUID();
        doThrow(ConflictException.build(
                "No se puede eliminar la organizacion porque esta siendo usada por una o mas aplicaciones."))
                .when(eliminarOrganizacion).execute(id);

        assertThrows(ConflictException.class, () -> interactor.execute(id));
    }
}