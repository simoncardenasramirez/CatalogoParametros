package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.consultarorganizacion.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ConsultarOrganizacionInteractorImplTest {

    @Mock
    private OrganizacionRepository organizacionRepository;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ConsultarOrganizacionInteractorImpl interactor;

    @BeforeEach
    void setUp() throws Exception {
        var campoConsultarMensaje = ConsultarOrganizacionInteractorImpl.class
                .getDeclaredField("consultarMensajePort");
        campoConsultarMensaje.setAccessible(true);
        campoConsultarMensaje.set(interactor, consultarMensajePort);
    }

    private OrganizacionEntity entidad(final String nombre) {
        return OrganizacionEntity.create(UUID.randomUUID(), nombre);
    }

    @Test
    void debeDevolverTodasLasEntidadesCuandoSeConsultaSinId() {
        var organizacion = entidad("organizacion");
        when(organizacionRepository.findAll()).thenReturn(List.of(organizacion));

        var resultado = interactor.execute();

        assertEquals(List.of(organizacion), resultado);
        verify(organizacionRepository).findAll();
    }

    @Test
    void debeDevolverEntidadesPaginadasCuandoLaPaginaYElTamanoSonValidos() {
        var organizacion = entidad("organizacion");
        var esperadas = List.of(organizacion);
        when(organizacionRepository.findAllPaginado(2, 5)).thenReturn(esperadas);

        var resultado = interactor.execute(2, 5);

        assertEquals(esperadas, resultado);
        verify(organizacionRepository).findAllPaginado(2, 5);
    }

    @Test
    void debeClamarLaPaginaAUnoCuandoEsInferiorAUno() {
        when(organizacionRepository.findAllPaginado(1, 5)).thenReturn(List.of());

        interactor.execute(0, 5);
        interactor.execute(-3, 5);

        verify(organizacionRepository, times(2)).findAllPaginado(1, 5);
    }

    @Test
    void debeClamarElTamanoDePaginaAUnoCuandoEsInferiorAUno() {
        when(organizacionRepository.findAllPaginado(2, 1)).thenReturn(List.of());

        interactor.execute(2, 0);
        interactor.execute(2, -1);

        verify(organizacionRepository, times(2)).findAllPaginado(2, 1);
    }

    @Test
    void debeUsarPaginaYTamanoMinimosCuandoAmbosSonInvalidos() {
        when(organizacionRepository.findAllPaginado(1, 1)).thenReturn(List.of());

        interactor.execute(0, 0);

        verify(organizacionRepository).findAllPaginado(1, 1);
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoNoExisteElId() {
        when(organizacionRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-98")).thenReturn("No existe la organizacion con el id especificado.");

        assertThrows(NotFoundException.class, () -> interactor.execute(UUID.randomUUID()));
    }
}