package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.interactor.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.ActualizarAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto.ActualizarAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;

@ExtendWith(MockitoExtension.class)
class ActualizarAplicacionInteractorImplTest {

    @Mock
    private ActualizarAplicacion actualizarAplicacion;

    @InjectMocks
    private ActualizarAplicacionInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseCuandoLosDatosSonValidos() {
        var id = UUID.randomUUID();
        var request = ActualizarAplicacionDtoRequest.create("aplicacion", UUID.randomUUID().toString(), "true",
                "2024-01-01 00:00:00", "2024-12-31 23:59:59");

        interactor.execute(id, request);

        verify(actualizarAplicacion).execute(any(ActualizarAplicacionDomain.class));
    }
}