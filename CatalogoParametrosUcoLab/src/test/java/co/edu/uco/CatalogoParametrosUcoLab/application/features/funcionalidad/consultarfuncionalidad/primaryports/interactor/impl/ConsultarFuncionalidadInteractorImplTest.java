package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.consultarfuncionalidad.primaryports.interactor.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;

@ExtendWith(MockitoExtension.class)
class ConsultarFuncionalidadInteractorImplTest {

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @InjectMocks
    private ConsultarFuncionalidadInteractorImpl interactor;

    private FuncionalidadEntity entidad(final String nombre) {
        return FuncionalidadEntity.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true,
                LocalDateTime.now(), null);
    }

    @Test
    void debeDevolverTodasLasEntidadesCuandoSeConsultaSinId() {
        var funcionalidad = entidad("funcionalidad");
        when(funcionalidadRepository.findAll()).thenReturn(List.of(funcionalidad));

        var resultado = interactor.execute();

        assertEquals(List.of(funcionalidad), resultado);
        verify(funcionalidadRepository).findAll();
    }

    @Test
    void debeDevolverEntidadesPaginadasCuandoLaPaginaYElTamanoSonValidos() {
        var funcionalidad = entidad("funcionalidad");
        var esperadas = List.of(funcionalidad);
        when(funcionalidadRepository.findAllPaginado(2, 5)).thenReturn(esperadas);

        var resultado = interactor.execute(2, 5);

        assertEquals(esperadas, resultado);
        verify(funcionalidadRepository).findAllPaginado(2, 5);
    }

    @Test
    void debeClamarLaPaginaAUnoCuandoEsInferiorAUno() {
        when(funcionalidadRepository.findAllPaginado(1, 5)).thenReturn(List.of());

        interactor.execute(0, 5);
        interactor.execute(-3, 5);

        verify(funcionalidadRepository, times(2)).findAllPaginado(1, 5);
    }

    @Test
    void debeClamarElTamanoDePaginaAUnoCuandoEsInferiorAUno() {
        when(funcionalidadRepository.findAllPaginado(2, 1)).thenReturn(List.of());

        interactor.execute(2, 0);
        interactor.execute(2, -1);

        verify(funcionalidadRepository, times(2)).findAllPaginado(2, 1);
    }

    @Test
    void debeUsarPaginaYTamanoMinimosCuandoAmbosSonInvalidos() {
        when(funcionalidadRepository.findAllPaginado(1, 1)).thenReturn(List.of());

        interactor.execute(0, 0);

        verify(funcionalidadRepository).findAllPaginado(1, 1);
    }

    @Test
    void debeDevolverListaVaciaCuandoNoExisteElId() {
        when(funcionalidadRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertTrue(interactor.execute(UUID.randomUUID()).isEmpty());
    }
}