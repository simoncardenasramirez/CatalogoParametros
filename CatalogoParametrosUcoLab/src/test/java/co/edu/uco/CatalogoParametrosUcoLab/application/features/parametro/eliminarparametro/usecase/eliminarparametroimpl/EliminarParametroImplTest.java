package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.usecase.eliminarparametroimpl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.event.EliminarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.publisher.EliminarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class EliminarParametroImplTest {

    @Mock
    private ParametroRepository parametroRepository;
    @Mock
    private EliminarParametroPublisher eliminarParametroPublisher;
    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private EliminarParametroImpl useCase;

    @BeforeEach
    void setUp() throws Exception {
        useCase = new EliminarParametroImpl(parametroRepository, eliminarParametroPublisher,
                new TelemetryService(new SimpleMeterRegistry()));
        var field = EliminarParametroImpl.class.getDeclaredField("consultarMensajePort");
        field.setAccessible(true);
        field.set(useCase, consultarMensajePort);
    }

    private ParametroEntity entidadConId(final UUID id) {
        return ParametroEntity.create(id, "parametro", UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debeEliminarParametroExitosamenteCuandoExisteElId() {
        var id = UUID.randomUUID();
        when(parametroRepository.findById(id)).thenReturn(Optional.of(entidadConId(id)));

        useCase.execute(id);

        verify(parametroRepository).deleteById(id);
        verify(eliminarParametroPublisher).sendEvent(any(EliminarParametroEvent.class));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-139"))
                .thenReturn("El id del parametro es obligatorio para eliminar.");

        var exception = assertThrows(ValidationException.class, () -> useCase.execute(null));

        assertEquals("El id del parametro es obligatorio para eliminar.", exception.getMessage());
        verify(parametroRepository, never()).findById(any());
        verify(parametroRepository, never()).deleteById(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-139"))
                .thenReturn("El id del parametro es obligatorio para eliminar.");

        var exception = assertThrows(ValidationException.class,
                () -> useCase.execute(UUIDHelper.getDefault()));

        assertEquals("El id del parametro es obligatorio para eliminar.", exception.getMessage());
        verify(parametroRepository, never()).findById(any());
        verify(parametroRepository, never()).deleteById(any());
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoNoExisteElParametro() {
        var id = UUID.randomUUID();
        when(parametroRepository.findById(id)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-138"))
                .thenReturn("No existe un parametro con el id especificado.");

        var exception = assertThrows(NotFoundException.class, () -> useCase.execute(id));

        assertEquals("No existe un parametro con el id especificado.", exception.getMessage());
        verify(parametroRepository, never()).deleteById(any());
        verify(eliminarParametroPublisher, never()).sendEvent(any());
    }
}