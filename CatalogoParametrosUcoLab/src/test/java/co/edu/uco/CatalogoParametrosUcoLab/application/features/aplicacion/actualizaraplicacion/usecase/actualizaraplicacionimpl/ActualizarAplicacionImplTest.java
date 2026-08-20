package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.actualizaraplicacionimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.ActualizarAplicacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.event.ActualizarAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class ActualizarAplicacionImplTest {

    @Mock
    private AplicacionRepository aplicacionRepository;

    @Mock
    private ActualizarAplicacionPublisher actualizarAplicacionPublisher;

    @Mock
    private ActualizarAplicacionRuleValidator actualizarAplicacionRuleValidator;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarAplicacionImpl useCase;

    @BeforeEach
    void setUp() throws Exception {
        useCase = new ActualizarAplicacionImpl(aplicacionRepository, actualizarAplicacionPublisher,
                actualizarAplicacionRuleValidator, new TelemetryService(new SimpleMeterRegistry()));
        Field campoMensaje = ActualizarAplicacionImpl.class.getDeclaredField("consultarMensajePort");
        campoMensaje.setAccessible(true);
        campoMensaje.set(useCase, consultarMensajePort);
    }

    private ActualizarAplicacionDomain domainValido() {
        return ActualizarAplicacionDomain.create(UUID.randomUUID(), "aplicacion", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debeActualizarAplicacionExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        var entidadExistente = AplicacionEntity.create(domain.getId(), "antiguo", UUID.randomUUID(), true, null, null);
        when(aplicacionRepository.findById(domain.getId())).thenReturn(Optional.of(entidadExistente));
        when(aplicacionRepository.update(any(AplicacionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(actualizarAplicacionRuleValidator).validate(domain);
        verify(aplicacionRepository).update(any(AplicacionEntity.class));
        verify(actualizarAplicacionPublisher).sendEvent(any(ActualizarAplicacionEvent.class));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-9"))
                .thenReturn("El id de la aplicacion es obligatorio para actualizar.");
        var domain = ActualizarAplicacionDomain.create(UUIDHelper.getDefault(), "aplicacion", UUID.randomUUID(),
                true, null, null);

        assertThrows(ValidationException.class, () -> useCase.execute(domain));
        verify(aplicacionRepository, never()).update(any());
        verify(actualizarAplicacionPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaAplicacionNoExiste() {
        when(consultarMensajePort.consultarMensaje("MSG-8"))
                .thenReturn("No existe una aplicacion con el id especificado.");
        var domain = domainValido();
        when(aplicacionRepository.findById(domain.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(domain));
        verify(aplicacionRepository, never()).update(any());
        verify(actualizarAplicacionPublisher, never()).sendEvent(any());
    }

    @Test
    void debeFallarCuandoLaValidacionFalla() {
        var domain = domainValido();
        var entidadExistente = AplicacionEntity.create(domain.getId(), "antiguo", UUID.randomUUID(), true, null, null);
        when(aplicacionRepository.findById(domain.getId())).thenReturn(Optional.of(entidadExistente));
        org.mockito.Mockito.doThrow(ValidationException.build("error de validacion"))
                .when(actualizarAplicacionRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));
        verify(aplicacionRepository, never()).update(any());
        verify(actualizarAplicacionPublisher, never()).sendEvent(any());
    }
}