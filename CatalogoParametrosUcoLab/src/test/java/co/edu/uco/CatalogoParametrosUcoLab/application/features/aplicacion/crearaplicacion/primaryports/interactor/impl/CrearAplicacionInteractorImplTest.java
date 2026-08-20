package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class CrearAplicacionInteractorImplTest {

    @Mock
    private CrearAplicacion crearAplicacion;

    @InjectMocks
    private CrearAplicacionInteractorImpl interactor;

    @BeforeEach
    void setUp() {
        interactor = new CrearAplicacionInteractorImpl(crearAplicacion, new TelemetryService(new SimpleMeterRegistry()));
    }

    private CrearAplicacionDtoRequest requestValido() {
        return CrearAplicacionDtoRequest.create("aplicacion", UUID.randomUUID().toString(), "true",
                "2024-01-01 00:00:00", "2024-12-31 23:59:59");
    }

    @Test
    void debeDelegarEnElUseCaseCuandoLosDatosSonValidos() {
        var request = requestValido();

        interactor.execute(request);

        verify(crearAplicacion).execute(any(CrearAplicacionDomain.class));
    }

    @Test
    void debeRelanzarLaExcepcionCuandoElUseCaseFalla() {
        var request = requestValido();
        org.mockito.Mockito.doThrow(ValidationException.build("error"))
                .when(crearAplicacion).execute(any(CrearAplicacionDomain.class));

        assertThrows(ValidationException.class, () -> interactor.execute(request));
    }
}