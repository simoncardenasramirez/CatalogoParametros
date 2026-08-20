package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.crearparametroimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.event.CrearParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class CrearParametroImplTest {

    @Mock
    private ParametroRepository parametroRepository;
    @Mock
    private CrearParametroPublisher crearParametroPublisher;
    @Mock
    private CrearParametroRuleValidator crearParametroRuleValidator;

    @InjectMocks
    private CrearParametroImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CrearParametroImpl(parametroRepository, crearParametroPublisher,
                crearParametroRuleValidator, new TelemetryService(new SimpleMeterRegistry()));
    }

    private CrearParametroDomain domainValido() {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), "parametro",
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debeCrearParametroExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        when(parametroRepository.save(any(ParametroEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(crearParametroRuleValidator).validate(domain);

        ArgumentCaptor<ParametroEntity> captor = ArgumentCaptor.forClass(ParametroEntity.class);
        verify(parametroRepository).save(captor.capture());
        assertNotEquals(UUIDHelper.getDefault(), captor.getValue().getId());
        assertEquals("parametro", captor.getValue().getNombre());

        verify(crearParametroPublisher).sendEvent(any(CrearParametroEvent.class));
    }

    @Test
    void debeFallarCuandoLaValidacionFallaSinGuardarNiPublicar() {
        var domain = domainValido();
        org.mockito.Mockito.doThrow(ValidationException.build("El nombre del parametro es obligatorio."))
                .when(crearParametroRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));

        verify(parametroRepository, never()).save(any());
        verify(crearParametroPublisher, never()).sendEvent(any());
    }
}