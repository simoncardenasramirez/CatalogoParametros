package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.actualizarorganizidadimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.ActualizarOrganizacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.event.ActualizarOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.publisher.ActualizarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class ActualizarOrganizacionImplTest {

    @Mock
    private OrganizacionRepository organizacionRepository;

    @Mock
    private ActualizarOrganizacionPublisher actualizarOrganizacionPublisher;

    @Mock
    private ActualizarOrganizacionRuleValidator actualizarOrganizacionRuleValidator;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private ActualizarOrganizacionImpl useCase;

    @BeforeEach
    void setUp() throws Exception {
        useCase = new ActualizarOrganizacionImpl(organizacionRepository, actualizarOrganizacionPublisher,
                new TelemetryService(new SimpleMeterRegistry()), actualizarOrganizacionRuleValidator);
        var campoConsultarMensaje = ActualizarOrganizacionImpl.class.getDeclaredField("consultarMensajePort");
        campoConsultarMensaje.setAccessible(true);
        campoConsultarMensaje.set(useCase, consultarMensajePort);
    }

    private ActualizarOrganizacionDomain domainValido() {
        return ActualizarOrganizacionDomain.create(UUID.randomUUID(), "organizacion");
    }

    @Test
    void debeActualizarOrganizacionExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        var existente = OrganizacionEntity.create(domain.getId(), "organizacion");
        when(organizacionRepository.findById(domain.getId())).thenReturn(Optional.of(existente));
        when(organizacionRepository.update(any(OrganizacionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(actualizarOrganizacionRuleValidator).validate(domain);
        verify(organizacionRepository).update(any(OrganizacionEntity.class));
        verify(actualizarOrganizacionPublisher).sendEvent(any(ActualizarOrganizacionEvent.class));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoNoExisteLaOrganizacionConElId() {
        var domain = domainValido();
        when(organizacionRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-93"))
                .thenReturn("La organizacion con id no existe.");

        assertThrows(NotFoundException.class, () -> useCase.execute(domain));
        verify(actualizarOrganizacionRuleValidator, never()).validate(any());
        verify(organizacionRepository, never()).update(any(OrganizacionEntity.class));
        verify(actualizarOrganizacionPublisher, never()).sendEvent(any());
    }

    @Test
    void debeFallarCuandoLaValidacionFalla() {
        var domain = domainValido();
        var existente = OrganizacionEntity.create(domain.getId(), "organizacion");
        when(organizacionRepository.findById(domain.getId())).thenReturn(Optional.of(existente));
        doThrow(ValidationException.build("El nombre de la organizacion no puede estar vacio."))
                .when(actualizarOrganizacionRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));
        verify(organizacionRepository, never()).update(any(OrganizacionEntity.class));
        verify(actualizarOrganizacionPublisher, never()).sendEvent(any());
    }
}