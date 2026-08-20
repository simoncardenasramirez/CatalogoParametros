package co.edu.uco.CatalogoParametrosUcoLab.application.features.tipoparametro.consultartipoparametro.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;

@ExtendWith(MockitoExtension.class)
class ConsultarTipoParametroInteractorImplTest {

    @Mock
    private TipoParametroRepository tipoParametroRepository;

    @InjectMocks
    private ConsultarTipoParametroInteractorImpl interactor;

    private TipoParametroEntity entidad(final String nombre) {
        return TipoParametroEntity.create(UUID.randomUUID(), nombre);
    }

    @Test
    void debeDevolverTodasLasEntidadesCuandoSeConsultaSinId() {
        var tipoParametro = entidad("Texto");
        when(tipoParametroRepository.findAll()).thenReturn(List.of(tipoParametro));

        var resultado = interactor.execute();

        assertEquals(List.of(tipoParametro), resultado);
        verify(tipoParametroRepository).findAll();
    }

    @Test
    void debeDevolverLaEntidadCuandoExisteElId() {
        var tipoParametro = entidad("Numero");
        when(tipoParametroRepository.findById(any(UUID.class))).thenReturn(Optional.of(tipoParametro));

        var resultado = interactor.execute(UUID.randomUUID());

        assertEquals(List.of(tipoParametro), resultado);
        verify(tipoParametroRepository).findById(any(UUID.class));
    }

    @Test
    void debeDevolverListaVaciaCuandoNoExisteElId() {
        when(tipoParametroRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertTrue(interactor.execute(UUID.randomUUID()).isEmpty());
    }
}