package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.crearfuncionalidadimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.event.CrearFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.publisher.CrearFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.CrearFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class CrearFuncionalidadImplTest {

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @Mock
    private CrearFuncionalidadPublisher crearFuncionalidadPublisher;

    @Mock
    private CrearFuncionalidadRuleValidator crearFuncionalidadRuleValidator;

    @InjectMocks
    private CrearFuncionalidadImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CrearFuncionalidadImpl(funcionalidadRepository, crearFuncionalidadPublisher,
                crearFuncionalidadRuleValidator, new TelemetryService(new SimpleMeterRegistry()));
    }

    private CrearFuncionalidadDomain domainValido() {
        return CrearFuncionalidadDomain.create(UUID.randomUUID(), "funcionalidad", UUID.randomUUID(), true,
                LocalDateTime.now(), null);
    }

    @Test
    void debeCrearFuncionalidadExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        when(funcionalidadRepository.save(any(FuncionalidadEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(crearFuncionalidadRuleValidator).validate(domain);
        verify(funcionalidadRepository).save(any(FuncionalidadEntity.class));
        verify(crearFuncionalidadPublisher).sendEvent(any(CrearFuncionalidadEvent.class));
    }

    @Test
    void debeFallarCuandoLaValidacionFalla() {
        var domain = domainValido();
        org.mockito.Mockito.doThrow(ValidationException.build("error de validacion"))
                .when(crearFuncionalidadRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));
        verify(funcionalidadRepository, never()).save(any());
        verify(crearFuncionalidadPublisher, never()).sendEvent(any());
    }
}