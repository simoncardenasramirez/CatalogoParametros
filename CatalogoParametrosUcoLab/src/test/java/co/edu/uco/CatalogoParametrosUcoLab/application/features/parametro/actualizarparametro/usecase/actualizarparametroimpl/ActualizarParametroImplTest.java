package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.actualizarparametroimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.ActualizarParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.event.ActualizarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class ActualizarParametroImplTest {

    @Mock
    private ParametroRepository parametroRepository;
    @Mock
    private ActualizarParametroPublisher actualizarParametroPublisher;
    @Mock
    private ActualizarParametroRuleValidator actualizarParametroRuleValidator;
    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private ActualizarParametroImpl useCase;

    @BeforeEach
    void setUp() throws Exception {
        useCase = new ActualizarParametroImpl(parametroRepository, actualizarParametroPublisher,
                actualizarParametroRuleValidator, new TelemetryService(new SimpleMeterRegistry()));
        ReflectionTestUtils.setField(useCase, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarParametroDomain domainValido() {
        return ActualizarParametroDomain.create(UUID.randomUUID(), "parametro",
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    private ParametroEntity entidadPara(final ActualizarParametroDomain domain) {
        return ParametroEntity.create(domain.getId(), domain.getNombre(), domain.getIdFuncionalidad(),
                domain.getIdTipoParametro(), domain.isActivo());
    }

    @Test
    void debeActualizarParametroExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        when(parametroRepository.findById(domain.getId()))
                .thenReturn(Optional.of(entidadPara(domain)));
        when(parametroRepository.update(any(ParametroEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(actualizarParametroRuleValidator).validate(domain);
        verify(parametroRepository).update(any(ParametroEntity.class));
        verify(actualizarParametroPublisher).sendEvent(any(ActualizarParametroEvent.class));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDomainEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-115"))
                .thenReturn("El id del parametro es obligatorio para actualizar.");

        var domain = ActualizarParametroDomain.create(UUIDHelper.getDefault(), "parametro",
                UUID.randomUUID(), UUID.randomUUID(), true);
        var exception = assertThrows(ValidationException.class, () -> useCase.execute(domain));

        assertEquals("El id del parametro es obligatorio para actualizar.", exception.getMessage());
        verify(parametroRepository, never()).findById(any());
        verify(parametroRepository, never()).update(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsElPorDefecto() {
        var domain = ActualizarParametroDomain.create(UUIDHelper.getDefault(), "parametro",
                UUID.randomUUID(), UUID.randomUUID(), true);
        when(consultarMensajePort.consultarMensaje("MSG-115"))
                .thenReturn("El id del parametro es obligatorio para actualizar.");

        var exception = assertThrows(ValidationException.class, () -> useCase.execute(domain));

        assertEquals("El id del parametro es obligatorio para actualizar.", exception.getMessage());
        verify(parametroRepository, never()).findById(any());
        verify(parametroRepository, never()).update(any());
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoNoExisteElParametro() {
        var domain = domainValido();
        when(parametroRepository.findById(domain.getId())).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-114"))
                .thenReturn("No existe un parametro con el id especificado.");

        var exception = assertThrows(NotFoundException.class, () -> useCase.execute(domain));

        assertEquals("No existe un parametro con el id especificado.", exception.getMessage());
        verify(actualizarParametroRuleValidator, never()).validate(any());
        verify(parametroRepository, never()).update(any());
        verify(actualizarParametroPublisher, never()).sendEvent(any());
    }

    @Test
    void debeFallarCuandoLaValidacionFallaSinActualizarNiPublicar() {
        var domain = domainValido();
        when(parametroRepository.findById(domain.getId()))
                .thenReturn(Optional.of(entidadPara(domain)));
        org.mockito.Mockito.doThrow(ValidationException.build("El nombre del parametro es obligatorio."))
                .when(actualizarParametroRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));

        verify(parametroRepository, never()).update(any());
        verify(actualizarParametroPublisher, never()).sendEvent(any());
    }
}