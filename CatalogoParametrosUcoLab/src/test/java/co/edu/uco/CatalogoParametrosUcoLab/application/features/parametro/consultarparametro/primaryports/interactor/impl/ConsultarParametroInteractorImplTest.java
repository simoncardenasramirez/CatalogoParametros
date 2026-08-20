package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametro.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;

@ExtendWith(MockitoExtension.class)
class ConsultarParametroInteractorImplTest {

    @Mock
    private ParametroRepository parametroRepository;

    @InjectMocks
    private ConsultarParametroInteractorImpl interactor;

    private ParametroEntity entidad(final String nombre) {
        return ParametroEntity.create(UUID.randomUUID(), nombre, UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debeDevolverTodasLasEntidadesCuandoSeConsultaSinId() {
        var parametro = entidad("parametro");
        when(parametroRepository.findAll()).thenReturn(List.of(parametro));

        var resultado = interactor.execute();

        assertEquals(List.of(parametro), resultado);
        verify(parametroRepository).findAll();
    }

    @Test
    void debeDevolverEntidadesPaginadasCuandoLaPaginaYElTamanoSonValidos() {
        var parametro = entidad("parametro");
        var esperadas = List.of(parametro);
        when(parametroRepository.findAllPaginado(2, 5)).thenReturn(esperadas);

        var resultado = interactor.execute(2, 5);

        assertEquals(esperadas, resultado);
        verify(parametroRepository).findAllPaginado(2, 5);
    }

    @Test
    void debeClamarLaPaginaAUnoCuandoEsInferiorAUno() {
        when(parametroRepository.findAllPaginado(1, 5)).thenReturn(List.of());

        interactor.execute(0, 5);
        interactor.execute(-3, 5);

        verify(parametroRepository, times(2)).findAllPaginado(1, 5);
    }

    @Test
    void debeClamarElTamanoDePaginaAUnoCuandoEsInferiorAUno() {
        when(parametroRepository.findAllPaginado(2, 1)).thenReturn(List.of());

        interactor.execute(2, 0);
        interactor.execute(2, -1);

        verify(parametroRepository, times(2)).findAllPaginado(2, 1);
    }

    @Test
    void debeUsarPaginaYTamanoMinimosCuandoAmbosSonInvalidos() {
        when(parametroRepository.findAllPaginado(1, 1)).thenReturn(List.of());

        interactor.execute(0, 0);

        verify(parametroRepository).findAllPaginado(1, 1);
    }

    @Test
    void debeDevolverListaConUnElementoCuandoExisteElId() {
        var parametro = entidad("parametro");
        when(parametroRepository.findById(any(UUID.class))).thenReturn(Optional.of(parametro));

        var resultado = interactor.execute(UUID.randomUUID());

        assertEquals(List.of(parametro), resultado);
    }

    @Test
    void debeDevolverListaVaciaCuandoNoExisteElId() {
        when(parametroRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertTrue(interactor.execute(UUID.randomUUID()).isEmpty());
    }
}