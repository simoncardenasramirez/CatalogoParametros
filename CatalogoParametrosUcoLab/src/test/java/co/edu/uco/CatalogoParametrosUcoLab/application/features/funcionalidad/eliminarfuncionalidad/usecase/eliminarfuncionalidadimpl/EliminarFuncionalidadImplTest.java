package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.eliminarfuncionalidadimpl;

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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.event.EliminarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.publisher.EliminarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.EliminarFuncionalidadIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.EliminarFuncionalidadIsNotUsedByParametroRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class EliminarFuncionalidadImplTest {

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @Mock
    private ParametroRepository parametroRepository;

    @Mock
    private EliminarFuncionalidadPublisher eliminarFuncionalidadPublisher;

    @Mock
    private EliminarFuncionalidadIdExistsRule idExistsRule;

    @Mock
    private EliminarFuncionalidadIsNotUsedByParametroRule isNotUsedByParametroRule;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private EliminarFuncionalidadImpl useCase;

    @BeforeEach
    void setUp() throws Exception {
        useCase = new EliminarFuncionalidadImpl(funcionalidadRepository, parametroRepository,
                eliminarFuncionalidadPublisher, idExistsRule, isNotUsedByParametroRule,
                new TelemetryService(new SimpleMeterRegistry()));
        Field campoMensaje = EliminarFuncionalidadImpl.class.getDeclaredField("consultarMensajePort");
        campoMensaje.setAccessible(true);
        campoMensaje.set(useCase, consultarMensajePort);
    }

    @Test
    void debeEliminarFuncionalidadExitosamenteCuandoLasReglasPasan() {
        var id = UUID.randomUUID();
        var entidad = FuncionalidadEntity.create(id, "funcionalidad", UUID.randomUUID(), true, null, null);
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.of(entidad));

        useCase.execute(id);

        verify(idExistsRule).execute(id);
        verify(isNotUsedByParametroRule).execute(id);
        verify(funcionalidadRepository).deleteById(id);
        verify(eliminarFuncionalidadPublisher).sendEvent(any(EliminarFuncionalidadEvent.class));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaReglaDeExistenciaFalla() {
        var id = UUID.randomUUID();
        org.mockito.Mockito.doThrow(NotFoundException.build("No existe una funcionalidad con el id especificado."))
                .when(idExistsRule).execute(id);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));
        verify(funcionalidadRepository, never()).deleteById(any());
        verify(eliminarFuncionalidadPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarConflictExceptionCuandoLaFuncionalidadEstaEnUso() {
        var id = UUID.randomUUID();
        org.mockito.Mockito
                .doThrow(ConflictException.build(
                        "No se puede eliminar la funcionalidad con el id porque esta siendo utilizada por uno o mas parametros."))
                .when(isNotUsedByParametroRule).execute(id);

        assertThrows(ConflictException.class, () -> useCase.execute(id));
        verify(funcionalidadRepository, never()).deleteById(any());
        verify(eliminarFuncionalidadPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-61"))
                .thenReturn("El id de la funcionalidad es obligatorio para eliminar.");

        assertThrows(ValidationException.class, () -> useCase.execute(UUIDHelper.getDefault()));
        verify(funcionalidadRepository, never()).deleteById(any());
        verify(eliminarFuncionalidadPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaFuncionalidadDesapareceAntesDeEliminar() {
        var id = UUID.randomUUID();
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-60"))
                .thenReturn("No existe una funcionalidad con el id especificado.");

        assertThrows(NotFoundException.class, () -> useCase.execute(id));
        verify(funcionalidadRepository, never()).deleteById(any());
        verify(eliminarFuncionalidadPublisher, never()).sendEvent(any());
    }
}