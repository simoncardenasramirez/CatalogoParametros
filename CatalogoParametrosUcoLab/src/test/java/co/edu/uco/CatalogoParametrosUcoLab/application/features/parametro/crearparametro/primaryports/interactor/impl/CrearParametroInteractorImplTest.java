package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearParametroInteractorImplTest {

    @Mock
    private CrearParametro crearParametro;

    @InjectMocks
    private CrearParametroInteractorImpl interactor;

    // ==================== TESTS DE EJECUCION ====================

    @Test
    void shouldExecuteCreateParametroSuccessfully() {
        var dtoRequest = CrearParametroDtoRequest.create(
                "ParametroTest",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "true"
        );

        assertDoesNotThrow(() -> interactor.execute(dtoRequest));

        verify(crearParametro, times(1)).execute(any());
    }

    @Test
    void shouldExecuteCreateParametroWithFalseActivo() {
        var dtoRequest = CrearParametroDtoRequest.create(
                "ParametroTest",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "false"
        );

        assertDoesNotThrow(() -> interactor.execute(dtoRequest));

        verify(crearParametro, times(1)).execute(any());
    }

    @Test
    void shouldExecuteCreateParametroWithNullRequest() {
        // Cuando se pasa null, el mapper crea un nuevo CrearParametroDtoRequest con valores por defecto
        // pero el constructor por defecto lanza excepcion porque nombre vacio no es valido
        assertThrows(ParametroException.class, () -> interactor.execute(null));
    }

    // ==================== TESTS DE INTERFAZ ====================

    @Test
    void shouldImplementCrearParametroInteractor() {
        assertInstanceOf(CrearParametroInteractor.class, interactor);
    }

    // ==================== TESTS DE INTEGRACION CON MAPPER ====================

    @Test
    void shouldMapDtoRequestToDomainAndExecute() {
        var idFuncionalidad = UUID.randomUUID();
        var idTipoParametro = UUID.randomUUID();
        var dtoRequest = CrearParametroDtoRequest.create(
                "ParametroTest",
                idFuncionalidad.toString(),
                idTipoParametro.toString(),
                "true"
        );

        interactor.execute(dtoRequest);

        verify(crearParametro).execute(argThat(domain -> {
            assertEquals("ParametroTest", domain.getNombre());
            assertEquals(idFuncionalidad, domain.getIdFuncionalidad());
            assertEquals(idTipoParametro, domain.getIdTipoParametro());
            assertTrue(domain.isActivo());
            return true;
        }));
    }

    @Test
    void shouldMapDtoRequestWithFalseActivoToDomain() {
        var dtoRequest = CrearParametroDtoRequest.create(
                "ParametroTest",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "false"
        );

        interactor.execute(dtoRequest);

        verify(crearParametro).execute(argThat(domain -> {
            assertFalse(domain.isActivo());
            return true;
        }));
    }

    // ==================== TESTS DE MANEJO DE EXCEPCIONES ====================

    @Test
    void shouldPropagateExceptionFromCreateParametro() {
        var dtoRequest = CrearParametroDtoRequest.create(
                "ParametroTest",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "true"
        );

        doThrow(new RuntimeException("Error al crear parametro"))
                .when(crearParametro).execute(any());

        assertThrows(RuntimeException.class, () -> interactor.execute(dtoRequest));
    }

    // ==================== TESTS DE DIFERENTES NOMBRES ====================

    @Test
    void shouldExecuteWithDifferentParameterNames() {
        var names = new String[]{"Parametro1", "Parametro_2", "Parametro.3", "Parametro-4", "ParametroTest123"};

        for (String nombre : names) {
            var dtoRequest = CrearParametroDtoRequest.create(
                    nombre,
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    "true"
            );

            interactor.execute(dtoRequest);
        }

        verify(crearParametro, times(5)).execute(any());
    }
}
