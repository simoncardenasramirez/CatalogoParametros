package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.crearorganizacionimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.event.CrearOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.publisher.CrearOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class CrearOrganizacionImplTest {

    @Mock
    private OrganizacionRepository organizacionRepository;

    @Mock
    private CrearOrganizacionPublisher crearOrganizacionPublisher;

    @Mock
    private CrearOrganizacionRuleValidator crearOrganizacionRuleValidator;

    private CrearOrganizacionImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CrearOrganizacionImpl(organizacionRepository, crearOrganizacionPublisher,
                crearOrganizacionRuleValidator, new TelemetryService(new SimpleMeterRegistry()));
    }

    private CrearOrganizacionDomain domainValido() {
        return CrearOrganizacionDomain.create(UUID.randomUUID(), "organizacion");
    }

    @Test
    void debeCrearOrganizacionExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        when(organizacionRepository.save(any(OrganizacionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(crearOrganizacionRuleValidator).validate(domain);
        verify(organizacionRepository).save(any(OrganizacionEntity.class));
        verify(crearOrganizacionPublisher).sendEvent(any(CrearOrganizacionEvent.class));
    }

    @Test
    void debeFallarCuandoLaValidacionFalla() {
        var domain = domainValido();
        doThrow(ValidationException.build("El nombre de la organizacion es obligatorio."))
                .when(crearOrganizacionRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));
        verify(organizacionRepository, never()).save(any(OrganizacionEntity.class));
        verify(crearOrganizacionPublisher, never()).sendEvent(any());
    }
}