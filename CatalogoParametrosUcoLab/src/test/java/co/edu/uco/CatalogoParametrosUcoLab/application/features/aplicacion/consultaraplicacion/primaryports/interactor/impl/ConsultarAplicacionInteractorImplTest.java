package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.consultaraplicacion.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;

@ExtendWith(MockitoExtension.class)
class ConsultarAplicacionInteractorImplTest {

    @Mock
    private AplicacionRepository aplicacionRepository;

    @InjectMocks
    private ConsultarAplicacionInteractorImpl interactor;

    private AplicacionEntity entidad(final String nombre) {
        return AplicacionEntity.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true,
                LocalDateTime.now(), null);
    }

    @Test
    void debeDevolverTodasLasEntidadesCuandoSeConsultaSinId() {
        var aplicacion = entidad("aplicacion");
        when(aplicacionRepository.findAll()).thenReturn(List.of(aplicacion));

        var resultado = interactor.execute();

        assertEquals(List.of(aplicacion), resultado);
        verify(aplicacionRepository).findAll();
    }

    @Test
    void debeDevolverEntidadesPaginadasCuandoLaPaginaYElTamanoSonValidos() {
        var aplicacion = entidad("aplicacion");
        var esperadas = List.of(aplicacion);
        when(aplicacionRepository.findAllPaginado(2, 5)).thenReturn(esperadas);

        var resultado = interactor.execute(2, 5);

        assertEquals(esperadas, resultado);
        verify(aplicacionRepository).findAllPaginado(2, 5);
    }

    @Test
    void debeClamarLaPaginaAUnoCuandoEsInferiorAUno() {
        when(aplicacionRepository.findAllPaginado(1, 5)).thenReturn(List.of());

        interactor.execute(0, 5);
        interactor.execute(-3, 5);

        verify(aplicacionRepository, times(2)).findAllPaginado(1, 5);
    }

    @Test
    void debeClamarElTamanoDePaginaAUnoCuandoEsInferiorAUno() {
        when(aplicacionRepository.findAllPaginado(2, 1)).thenReturn(List.of());

        interactor.execute(2, 0);
        interactor.execute(2, -1);

        verify(aplicacionRepository, times(2)).findAllPaginado(2, 1);
    }

    @Test
    void debeUsarPaginaYTamanoMinimosCuandoAmbosSonInvalidos() {
        when(aplicacionRepository.findAllPaginado(1, 1)).thenReturn(List.of());

        interactor.execute(0, 0);

        verify(aplicacionRepository).findAllPaginado(1, 1);
    }

    @Test
    void debeDevolverListaVaciaCuandoNoExisteElId() {
        when(aplicacionRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertTrue(interactor.execute(UUID.randomUUID()).isEmpty());
    }
}