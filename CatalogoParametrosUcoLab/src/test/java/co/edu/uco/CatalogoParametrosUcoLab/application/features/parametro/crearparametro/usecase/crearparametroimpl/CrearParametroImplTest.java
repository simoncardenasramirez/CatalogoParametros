package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.crearparametroimpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.event.CrearParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearParametroImplTest {

    @Mock
    private ParametroRepository parametroRepository;

    @Mock
    private CrearParametroPublisher crearParametroPublisher;

    @Mock
    private CrearParametroRuleValidator crearParametroRuleValidator;

    @InjectMocks
    private CrearParametroImpl crearParametro;

    // ==================== TESTS DE CREACION EXITOSA ====================

    @Test
    void shouldCreateParametroSuccessfully() {
        var domain = CrearParametroDomain.create(null, "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);
        var savedEntity = ParametroEntity.create(UUID.randomUUID(), "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        when(parametroRepository.save(any(ParametroEntity.class))).thenReturn(savedEntity);

        assertDoesNotThrow(() -> crearParametro.execute(domain));

        verify(parametroRepository, times(1)).save(any(ParametroEntity.class));
        verify(crearParametroPublisher, times(1)).sendEvent(any(CrearParametroEvent.class));
        verify(crearParametroRuleValidator, times(1)).validate(domain);
    }

    @Test
    void shouldGenerateIdBeforeSaving() {
        var domain = CrearParametroDomain.create(null, "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);
        var savedEntity = ParametroEntity.create(UUID.randomUUID(), "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        when(parametroRepository.save(any(ParametroEntity.class))).thenReturn(savedEntity);

        crearParametro.execute(domain);

        // Verificar que el ID fue generado (no es null despues de execute)
        assertNotNull(domain.getId());
    }

    @Test
    void shouldCreateParametroWithInactiveStatus() {
        var domain = CrearParametroDomain.create(null, "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), false);
        var savedEntity = ParametroEntity.create(UUID.randomUUID(), "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), false);

        when(parametroRepository.save(any(ParametroEntity.class))).thenReturn(savedEntity);

        assertDoesNotThrow(() -> crearParametro.execute(domain));

        verify(parametroRepository).save(argThat(entity -> !entity.isActivo()));
    }

    // ==================== TESTS DE VALIDACION ====================

    @Test
    void shouldValidateBeforeCreating() {
        var domain = CrearParametroDomain.create(null, "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        crearParametro.execute(domain);

        verify(crearParametroRuleValidator, times(1)).validate(domain);
    }

    @Test
    void shouldThrowExceptionWhenValidationFails() {
        var domain = CrearParametroDomain.create(null, "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        doThrow(new RuntimeException("Validacion fallida"))
                .when(crearParametroRuleValidator).validate(any());

        assertThrows(RuntimeException.class, () -> crearParametro.execute(domain));

        verify(parametroRepository, never()).save(any());
        verify(crearParametroPublisher, never()).sendEvent(any());
    }

    // ==================== TESTS DE PUBLICACION DE EVENTOS ====================

    @Test
    void shouldPublishCreatedEvent() {
        var domain = CrearParametroDomain.create(null, "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);
        var savedEntity = ParametroEntity.create(UUID.randomUUID(), "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        when(parametroRepository.save(any(ParametroEntity.class))).thenReturn(savedEntity);

        crearParametro.execute(domain);

        verify(crearParametroPublisher).sendEvent(argThat(event -> {
            assertInstanceOf(CrearParametroEvent.class, event);
            assertEquals(savedEntity, event.getParametro());
            return true;
        }));
    }

    @Test
    void shouldNotPublishEventWhenRepositoryFails() {
        var domain = CrearParametroDomain.create(null, "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        when(parametroRepository.save(any(ParametroEntity.class))).thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> crearParametro.execute(domain));

        verify(crearParametroPublisher, never()).sendEvent(any());
    }

    // ==================== TESTS DE ENTIDAD PARAMETRO ====================

    @Test
    void shouldCreateEntityWithCorrectData() {
        var id = UUID.randomUUID();
        var nombre = "ParametroTest";
        var idFuncionalidad = UUID.randomUUID();
        var idTipoParametro = UUID.randomUUID();
        var activo = true;

        var domain = CrearParametroDomain.create(id, nombre, idFuncionalidad, idTipoParametro, activo);
        var savedEntity = ParametroEntity.create(UUID.randomUUID(), nombre, idFuncionalidad, idTipoParametro, activo);

        when(parametroRepository.save(any(ParametroEntity.class))).thenReturn(savedEntity);

        crearParametro.execute(domain);

        verify(parametroRepository).save(argThat(entity -> {
            assertEquals(nombre, entity.getNombre());
            assertEquals(idFuncionalidad, entity.getIdFuncionalidad());
            assertEquals(idTipoParametro, entity.getIdTipoParametro());
            assertTrue(entity.isActivo());
            return true;
        }));
    }

    @Test
    void shouldCreateEntityWithGeneratedId() {
        var domain = CrearParametroDomain.create(null, "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);
        var savedEntity = ParametroEntity.create(UUID.randomUUID(), "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        when(parametroRepository.save(any(ParametroEntity.class))).thenReturn(savedEntity);

        crearParametro.execute(domain);

        verify(parametroRepository).save(argThat(entity -> {
            assertNotNull(entity.getId());
            return true;
        }));
    }

    // ==================== TESTS DE IMPLEMENTACION ====================

    @Test
    void shouldImplementCrearParametroInterface() {
        assertInstanceOf(CrearParametro.class, crearParametro);
    }

    @Test
    void shouldBeAnnotatedAsService() {
        assertTrue(crearParametro.getClass().isAnnotationPresent(org.springframework.stereotype.Service.class));
    }

    // ==================== TESTS DE FLUJO COMPLETO ====================

    @Test
    void shouldCompleteFullFlowSuccessfully() {
        var domain = CrearParametroDomain.create(null, "ParametroCompleto", UUID.randomUUID(), UUID.randomUUID(), true);
        var savedEntity = ParametroEntity.create(UUID.randomUUID(), "ParametroCompleto", UUID.randomUUID(), UUID.randomUUID(), true);

        when(parametroRepository.save(any(ParametroEntity.class))).thenReturn(savedEntity);

        assertDoesNotThrow(() -> crearParametro.execute(domain));

        // Verificar el flujo completo
        verify(crearParametroRuleValidator).validate(domain);
        verify(parametroRepository).save(any(ParametroEntity.class));
        verify(crearParametroPublisher).sendEvent(any(CrearParametroEvent.class));
    }

    @Test
    void shouldHandleMultipleCreations() {
        var domain1 = CrearParametroDomain.create(null, "Parametro1", UUID.randomUUID(), UUID.randomUUID(), true);
        var domain2 = CrearParametroDomain.create(null, "Parametro2", UUID.randomUUID(), UUID.randomUUID(), true);
        var savedEntity1 = ParametroEntity.create(UUID.randomUUID(), "Parametro1", UUID.randomUUID(), UUID.randomUUID(), true);
        var savedEntity2 = ParametroEntity.create(UUID.randomUUID(), "Parametro2", UUID.randomUUID(), UUID.randomUUID(), true);

        when(parametroRepository.save(any(ParametroEntity.class)))
                .thenReturn(savedEntity1)
                .thenReturn(savedEntity2);

        assertDoesNotThrow(() -> {
            crearParametro.execute(domain1);
            crearParametro.execute(domain2);
        });

        verify(parametroRepository, times(2)).save(any(ParametroEntity.class));
        verify(crearParametroPublisher, times(2)).sendEvent(any(CrearParametroEvent.class));
    }
}
