package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.actualizarmoduloimpl;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.ActualizarModuloRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.event.ActualizarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class ActualizarModuloImplTest {

    @Mock
    private ModuloRepository moduloRepository;

    @Mock
    private ActualizarModuloPublisher actualizarModuloPublisher;

    @Mock
    private ActualizarModuloRuleValidator actualizarModuloRuleValidator;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarModuloImpl useCase;

    @BeforeEach
    void setUp() {
        try {
            var campo = ActualizarModuloImpl.class.getDeclaredField("telemetryService");
            campo.setAccessible(true);
            campo.set(useCase, new TelemetryService(new SimpleMeterRegistry()));
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException("No se pudo inyectar el TelemetryService real", exception);
        }
        ReflectionTestUtils.setField(useCase, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarModuloDomain domainValido() {
        return ActualizarModuloDomain.create(UUID.randomUUID(), "modulo", UUID.randomUUID(), true, null, null);
    }

    private ModuloEntity entidadDelDominio(final ActualizarModuloDomain domain) {
        return ModuloEntity.create(domain.getId(), domain.getNombre(), domain.getIdAplicacion(),
                domain.isActivo(), domain.getFechaInicio(), domain.getFechaFinal());
    }

    @Test
    void debeActualizarModuloExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        when(moduloRepository.findById(domain.getId())).thenReturn(Optional.of(entidadDelDominio(domain)));
        when(moduloRepository.update(any(ModuloEntity.class))).thenReturn(entidadDelDominio(domain));

        useCase.execute(domain);

        verify(actualizarModuloRuleValidator).validate(domain);
        verify(moduloRepository).update(any(ModuloEntity.class));
        verify(actualizarModuloPublisher).sendEvent(any(ActualizarModuloEvent.class));
    }

    @Test
    void debeFallarCuandoLaValidacionFalla() {
        var domain = domainValido();
        when(moduloRepository.findById(domain.getId())).thenReturn(Optional.of(entidadDelDominio(domain)));
        doThrow(ValidationException.build("error")).when(actualizarModuloRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));

        verify(moduloRepository, never()).update(any());
        verify(actualizarModuloPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoElModuloNoExiste() {
        var domain = domainValido();
        when(moduloRepository.findById(domain.getId())).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-69")).thenReturn("No existe un modulo con el id especificado.");

        assertThrows(NotFoundException.class, () -> useCase.execute(domain));

        verify(actualizarModuloRuleValidator, never()).validate(any());
        verify(moduloRepository, never()).update(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsElPorDefecto() {
        var domain = ActualizarModuloDomain.create(UUIDHelper.getDefault(), "modulo", UUID.randomUUID(), true, null, null);
        when(consultarMensajePort.consultarMensaje("MSG-70"))
                .thenReturn("El id del modulo es obligatorio para actualizar.");

        assertThrows(ValidationException.class, () -> useCase.execute(domain));

        verify(moduloRepository, never()).findById(any());
        verify(moduloRepository, never()).update(any());
    }
}