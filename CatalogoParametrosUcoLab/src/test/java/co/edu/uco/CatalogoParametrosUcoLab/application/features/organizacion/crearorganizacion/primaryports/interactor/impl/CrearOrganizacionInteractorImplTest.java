package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class CrearOrganizacionInteractorImplTest {

    @Mock
    private CrearOrganizacion crearOrganizacion;

    @InjectMocks
    private CrearOrganizacionInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseConElDomainMapeado() {
        interactor.execute(CrearOrganizacionDtoRequest.create("organizacion"));

        var captor = ArgumentCaptor.forClass(CrearOrganizacionDomain.class);
        verify(crearOrganizacion).execute(captor.capture());
        assertEquals("organizacion", captor.getValue().getNombre());
    }

    @Test
    void debeRelanzarLaExcepcionCuandoElUseCaseFalla() {
        doThrow(ValidationException.build("El nombre de la organizacion es obligatorio."))
                .when(crearOrganizacion).execute(any(CrearOrganizacionDomain.class));

        assertThrows(ValidationException.class,
                () -> interactor.execute(CrearOrganizacionDtoRequest.create("organizacion")));
    }
}