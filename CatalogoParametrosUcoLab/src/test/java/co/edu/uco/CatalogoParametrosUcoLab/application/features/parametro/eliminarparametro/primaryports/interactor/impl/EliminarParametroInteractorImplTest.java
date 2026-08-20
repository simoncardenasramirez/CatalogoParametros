package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.primaryports.interactor.impl;

import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.EliminarParametro;

@ExtendWith(MockitoExtension.class)
class EliminarParametroInteractorImplTest {

    @Mock
    private EliminarParametro eliminarParametro;

    @InjectMocks
    private EliminarParametroInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseConElIdRecibido() {
        var id = UUID.randomUUID();

        interactor.execute(id);

        verify(eliminarParametro).execute(id);
    }
}