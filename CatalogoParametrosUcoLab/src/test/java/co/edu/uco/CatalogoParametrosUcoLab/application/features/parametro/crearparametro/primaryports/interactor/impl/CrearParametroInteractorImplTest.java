package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;

@ExtendWith(MockitoExtension.class)
class CrearParametroInteractorImplTest {

    @Mock
    private CrearParametro crearParametro;

    @InjectMocks
    private CrearParametroInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseConElDomainMapeado() {
        var request = CrearParametroDtoRequest.create("parametro", UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), "true");

        interactor.execute(request);

        ArgumentCaptor<CrearParametroDomain> captor = ArgumentCaptor.forClass(CrearParametroDomain.class);
        verify(crearParametro).execute(captor.capture());
        assertEquals("parametro", captor.getValue().getNombre());
        assertTrue(captor.getValue().isActivo());
    }
}