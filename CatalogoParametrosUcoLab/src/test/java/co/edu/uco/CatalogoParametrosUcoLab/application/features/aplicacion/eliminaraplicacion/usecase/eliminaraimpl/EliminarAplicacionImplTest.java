package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.eliminaraimpl;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.event.EliminarAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.publisher.EliminarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.EliminarAplicacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.EliminarAplicacionIsNotUsedByModuloRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class EliminarAplicacionImplTest {

    @Mock
    private AplicacionRepository aplicacionRepository;

    @Mock
    private EliminarAplicacionPublisher eliminarAplicacionPublisher;

    @Mock
    private EliminarAplicacionIdExistsRule idExistsRule;

    @Mock
    private EliminarAplicacionIsNotUsedByModuloRule isNotUsedByModuloRule;

    @InjectMocks
    private EliminarAplicacionImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new EliminarAplicacionImpl(aplicacionRepository, eliminarAplicacionPublisher,
                idExistsRule, isNotUsedByModuloRule, new TelemetryService(new SimpleMeterRegistry()));
    }

    @Test
    void debeEliminarAplicacionExitosamenteCuandoLasReglasPasan() {
        var id = UUID.randomUUID();
        var entidad = AplicacionEntity.create(id, "aplicacion", UUID.randomUUID(), true, null, null);
        when(aplicacionRepository.findById(id)).thenReturn(Optional.of(entidad));

        useCase.execute(id);

        verify(idExistsRule).execute(id);
        verify(isNotUsedByModuloRule).execute(id);
        verify(aplicacionRepository).deleteById(id);
        verify(eliminarAplicacionPublisher).sendEvent(any(EliminarAplicacionEvent.class));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaAplicacionNoExiste() {
        var id = UUID.randomUUID();
        org.mockito.Mockito.doThrow(NotFoundException.build("La aplicacion con id no existe."))
                .when(idExistsRule).execute(id);

        var exception = assertThrows(ValidationException.class, () -> useCase.execute(id));
        assertEquals("La aplicacion con id no existe.", exception.getMessage());
        verify(aplicacionRepository, never()).deleteById(any());
        verify(eliminarAplicacionPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaAplicacionEstaSiendoUsadaPorModulos() {
        var id = UUID.randomUUID();
        org.mockito.Mockito.doThrow(ConflictException.build("No se puede eliminar la aplicacion porque esta siendo usada por uno o mas modulos."))
                .when(isNotUsedByModuloRule).execute(id);

        assertThrows(ValidationException.class, () -> useCase.execute(id));
        verify(aplicacionRepository, never()).deleteById(any());
        verify(eliminarAplicacionPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarValidationExceptionConAmbosMensajesCuandoLasDosReglasFallan() {
        var id = UUID.randomUUID();
        org.mockito.Mockito.doThrow(NotFoundException.build("La aplicacion con id no existe."))
                .when(idExistsRule).execute(id);
        org.mockito.Mockito.doThrow(ConflictException.build("No se puede eliminar la aplicacion porque esta siendo usada por uno o mas modulos."))
                .when(isNotUsedByModuloRule).execute(id);

        var exception = assertThrows(ValidationException.class, () -> useCase.execute(id));
        assertEquals("La aplicacion con id no existe., No se puede eliminar la aplicacion porque esta siendo usada por uno o mas modulos.",
                exception.getMessage());
        verify(aplicacionRepository, never()).deleteById(any());
    }
}