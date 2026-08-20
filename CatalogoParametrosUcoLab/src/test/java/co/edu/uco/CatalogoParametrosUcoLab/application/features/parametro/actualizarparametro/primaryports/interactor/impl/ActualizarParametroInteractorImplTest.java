package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.ActualizarParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;

@ExtendWith(MockitoExtension.class)
class ActualizarParametroInteractorImplTest {

    @Mock
    private ActualizarParametro actualizarParametro;

    @InjectMocks
    private ActualizarParametroInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseConElIdYElDomainMapeado() {
        var id = UUID.randomUUID();
        var request = ActualizarParametroDtoRequest.create("parametro", UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), "true");

        interactor.execute(id, request);

        ArgumentCaptor<ActualizarParametroDomain> captor = ArgumentCaptor.forClass(ActualizarParametroDomain.class);
        verify(actualizarParametro).execute(captor.capture());
        assertEquals(id, captor.getValue().getId());
        assertEquals("parametro", captor.getValue().getNombre());
        assertTrue(captor.getValue().isActivo());
    }
}