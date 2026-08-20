package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.crearaplicacionimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.event.CrearAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.publisher.CrearAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class CrearAplicacionImplTest {

    @Mock
    private AplicacionRepository aplicacionRepository;

    @Mock
    private CrearAplicacionPublisher crearAplicacionPublisher;

    @Mock
    private CrearAplicacionRuleValidator crearAplicacionRuleValidator;

    @InjectMocks
    private CrearAplicacionImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CrearAplicacionImpl(aplicacionRepository, crearAplicacionPublisher,
                crearAplicacionRuleValidator, new TelemetryService(new SimpleMeterRegistry()));
    }

    private CrearAplicacionDomain domainValido() {
        return CrearAplicacionDomain.create(UUID.randomUUID(), "aplicacion", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debeCrearAplicacionExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        when(aplicacionRepository.save(any(AplicacionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(crearAplicacionRuleValidator).validate(domain);
        verify(aplicacionRepository).save(any(AplicacionEntity.class));
        verify(crearAplicacionPublisher).sendEvent(any(CrearAplicacionEvent.class));
    }

    @Test
    void debeFallarCuandoLaValidacionFalla() {
        var domain = domainValido();
        org.mockito.Mockito.doThrow(ValidationException.build("error de validacion"))
                .when(crearAplicacionRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));
        verify(aplicacionRepository, never()).save(any());
        verify(crearAplicacionPublisher, never()).sendEvent(any());
    }
}