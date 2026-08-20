package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.actualizarfuncionalidadimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.event.ActualizarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class ActualizarFuncionalidadImplTest {

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @Mock
    private ActualizarFuncionalidadPublisher actualizarFuncionalidadPublisher;

    @Mock
    private ActualizarFuncionalidadRuleValidator actualizarFuncionalidadRuleValidator;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarFuncionalidadImpl useCase;

    @BeforeEach
    void setUp() throws Exception {
        useCase = new ActualizarFuncionalidadImpl(funcionalidadRepository, actualizarFuncionalidadPublisher,
                actualizarFuncionalidadRuleValidator, new TelemetryService(new SimpleMeterRegistry()));
        Field campoMensaje = ActualizarFuncionalidadImpl.class.getDeclaredField("consultarMensajePort");
        campoMensaje.setAccessible(true);
        campoMensaje.set(useCase, consultarMensajePort);
    }

    private ActualizarFuncionalidadDomain domainValido() {
        return ActualizarFuncionalidadDomain.create(UUID.randomUUID(), "funcionalidad", UUID.randomUUID(), true,
                LocalDateTime.now(), null);
    }

    @Test
    void debeActualizarFuncionalidadExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        var entidadExistente = FuncionalidadEntity.create(domain.getId(), "antiguo", UUID.randomUUID(), true, null,
                null);
        when(funcionalidadRepository.findById(domain.getId())).thenReturn(Optional.of(entidadExistente));
        when(funcionalidadRepository.update(any(FuncionalidadEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(actualizarFuncionalidadRuleValidator).validate(domain);
        verify(funcionalidadRepository).update(any(FuncionalidadEntity.class));
        verify(actualizarFuncionalidadPublisher).sendEvent(any(ActualizarFuncionalidadEvent.class));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-37"))
                .thenReturn("El id de la funcionalidad es obligatorio para actualizar.");
        var domain = ActualizarFuncionalidadDomain.create(UUIDHelper.getDefault(), "funcionalidad",
                UUID.randomUUID(), true, null, null);

        assertThrows(ValidationException.class, () -> useCase.execute(domain));
        verify(funcionalidadRepository, never()).update(any());
        verify(actualizarFuncionalidadPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaFuncionalidadNoExiste() {
        when(consultarMensajePort.consultarMensaje("MSG-36"))
                .thenReturn("No existe una funcionalidad con el id especificado.");
        var domain = domainValido();
        when(funcionalidadRepository.findById(domain.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(domain));
        verify(funcionalidadRepository, never()).update(any());
        verify(actualizarFuncionalidadPublisher, never()).sendEvent(any());
    }

    @Test
    void debeFallarCuandoLaValidacionFalla() {
        var domain = domainValido();
        var entidadExistente = FuncionalidadEntity.create(domain.getId(), "antiguo", UUID.randomUUID(), true, null,
                null);
        when(funcionalidadRepository.findById(domain.getId())).thenReturn(Optional.of(entidadExistente));
        org.mockito.Mockito.doThrow(ValidationException.build("error de validacion"))
                .when(actualizarFuncionalidadRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));
        verify(funcionalidadRepository, never()).update(any());
        verify(actualizarFuncionalidadPublisher, never()).sendEvent(any());
    }
}