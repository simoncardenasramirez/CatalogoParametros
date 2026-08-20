package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.consultarmodulo.primaryports.interactor.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;

@ExtendWith(MockitoExtension.class)
class ConsultarModuloInteractorImplTest {

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private ConsultarModuloInteractorImpl interactor;

    private ModuloEntity entidad(final String nombre) {
        return ModuloEntity.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true,
                LocalDateTime.now(), null);
    }

    @Test
    void debeDevolverTodasLasEntidadesCuandoSeConsultaSinId() {
        var modulo = entidad("modulo");
        when(moduloRepository.findAll()).thenReturn(List.of(modulo));

        var resultado = interactor.execute();

        assertEquals(List.of(modulo), resultado);
        verify(moduloRepository).findAll();
    }

    @Test
    void debeDevolverEntidadesPaginadasCuandoLaPaginaYElTamanoSonValidos() {
        var modulo = entidad("modulo");
        var esperadas = List.of(modulo);
        when(moduloRepository.findAllPaginado(2, 5)).thenReturn(esperadas);

        var resultado = interactor.execute(2, 5);

        assertEquals(esperadas, resultado);
        verify(moduloRepository).findAllPaginado(2, 5);
    }

    @Test
    void debeClamarLaPaginaAUnoCuandoEsInferiorAUno() {
        when(moduloRepository.findAllPaginado(1, 5)).thenReturn(List.of());

        interactor.execute(0, 5);
        interactor.execute(-3, 5);

        verify(moduloRepository, times(2)).findAllPaginado(1, 5);
    }

    @Test
    void debeClamarElTamanoDePaginaAUnoCuandoEsInferiorAUno() {
        when(moduloRepository.findAllPaginado(2, 1)).thenReturn(List.of());

        interactor.execute(2, 0);
        interactor.execute(2, -1);

        verify(moduloRepository, times(2)).findAllPaginado(2, 1);
    }

    @Test
    void debeUsarPaginaYTamanoMinimosCuandoAmbosSonInvalidos() {
        when(moduloRepository.findAllPaginado(1, 1)).thenReturn(List.of());

        interactor.execute(0, 0);

        verify(moduloRepository).findAllPaginado(1, 1);
    }

    @Test
    void debeDevolverListaVaciaCuandoNoExisteElId() {
        when(moduloRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertTrue(interactor.execute(UUID.randomUUID()).isEmpty());
    }
}