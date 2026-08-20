package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.ActualizarOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ActualizarOrganizacionInteractorImplTest {

    @Mock
    private ActualizarOrganizacion actualizarOrganizacion;

    @InjectMocks
    private ActualizarOrganizacionInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseConElIdYElDomainMapeado() {
        var id = UUID.randomUUID();

        interactor.execute(id, ActualizarOrganizacionDtoRequest.create("organizacion"));

        var captor = ArgumentCaptor.forClass(ActualizarOrganizacionDomain.class);
        verify(actualizarOrganizacion).execute(captor.capture());
        assertEquals(id, captor.getValue().getId());
        assertEquals("organizacion", captor.getValue().getNombre());
    }

    @Test
    void debeRelanzarLaExcepcionCuandoElUseCaseFalla() {
        doThrow(NotFoundException.build("La organizacion con id no existe."))
                .when(actualizarOrganizacion).execute(any(ActualizarOrganizacionDomain.class));

        assertThrows(NotFoundException.class,
                () -> interactor.execute(UUID.randomUUID(),
                        ActualizarOrganizacionDtoRequest.create("organizacion")));
    }
}