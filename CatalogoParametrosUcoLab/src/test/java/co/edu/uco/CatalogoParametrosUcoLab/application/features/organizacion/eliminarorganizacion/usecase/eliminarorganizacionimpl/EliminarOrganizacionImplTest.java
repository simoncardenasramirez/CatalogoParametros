package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.eliminarorganizacionimpl;

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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.event.EliminarOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.publisher.EliminarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.EliminarOrganizacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.EliminarOrganizacionIsNotUsedByAplicacionRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class EliminarOrganizacionImplTest {

    @Mock
    private OrganizacionRepository organizacionRepository;

    @Mock
    private EliminarOrganizacionPublisher eliminarOrganizacionPublisher;

    @Mock
    private EliminarOrganizacionIdExistsRule idExistsRule;

    @Mock
    private EliminarOrganizacionIsNotUsedByAplicacionRule isNotUsedByAplicacionRule;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private EliminarOrganizacionImpl useCase;

    @BeforeEach
    void setUp() throws Exception {
        useCase = new EliminarOrganizacionImpl(organizacionRepository, eliminarOrganizacionPublisher,
                idExistsRule, isNotUsedByAplicacionRule, new TelemetryService(new SimpleMeterRegistry()));
        var campoConsultarMensaje = EliminarOrganizacionImpl.class.getDeclaredField("consultarMensajePort");
        campoConsultarMensaje.setAccessible(true);
        campoConsultarMensaje.set(useCase, consultarMensajePort);
    }

    @Test
    void debeEliminarOrganizacionExitosamenteCuandoExisteYNoEstaEnUso() {
        var id = UUID.randomUUID();
        var existente = OrganizacionEntity.create(id, "organizacion");
        when(organizacionRepository.findById(id)).thenReturn(Optional.of(existente));

        useCase.execute(id);

        verify(organizacionRepository).deleteById(id);
        verify(eliminarOrganizacionPublisher).sendEvent(any(EliminarOrganizacionEvent.class));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaOrganizacionEstaEnUso() {
        var id = UUID.randomUUID();
        doThrow(ConflictException.build(
                "No se puede eliminar la organizacion porque esta siendo usada por una o mas aplicaciones."))
                .when(isNotUsedByAplicacionRule).execute(id);

        assertThrows(ValidationException.class, () -> useCase.execute(id));
        verify(organizacionRepository, never()).deleteById(any(UUID.class));
        verify(eliminarOrganizacionPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarValidationExceptionCuandoNoExisteLaOrganizacionConElId() {
        var id = UUID.randomUUID();
        doThrow(NotFoundException.build("La organizacion con id no existe."))
                .when(idExistsRule).execute(id);

        assertThrows(ValidationException.class, () -> useCase.execute(id));
        verify(organizacionRepository, never()).deleteById(any(UUID.class));
        verify(eliminarOrganizacionPublisher, never()).sendEvent(any());
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaOrganizacionDesapareceAntesDeEliminar() {
        var id = UUID.randomUUID();
        when(organizacionRepository.findById(id)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-106"))
                .thenReturn("No existe una organizacion con el id especificado.");

        assertThrows(NotFoundException.class, () -> useCase.execute(id));
        verify(organizacionRepository, never()).deleteById(any(UUID.class));
        verify(eliminarOrganizacionPublisher, never()).sendEvent(any());
    }
}