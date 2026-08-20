package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.ActualizarModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto.ActualizarModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;

@ExtendWith(MockitoExtension.class)
class ActualizarModuloInteractorImplTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mock
    private ActualizarModulo actualizarModulo;

    @InjectMocks
    private ActualizarModuloInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseConElIdYElDominioMapeado() {
        var id = UUID.randomUUID();
        var idAplicacion = UUID.randomUUID().toString();
        var request = ActualizarModuloDtoRequest.create("modulo", idAplicacion, "true",
                "2026-01-01 00:00:00", "2026-12-31 23:59:59");

        interactor.execute(id, request);

        ArgumentCaptor<ActualizarModuloDomain> captor = ArgumentCaptor.forClass(ActualizarModuloDomain.class);
        verify(actualizarModulo).execute(captor.capture());
        var domain = captor.getValue();
        assertEquals(id, domain.getId());
        assertEquals("modulo", domain.getNombre());
        assertEquals(UUID.fromString(idAplicacion), domain.getIdAplicacion());
        assertTrue(domain.isActivo());
        assertEquals(LocalDateTime.parse("2026-01-01 00:00:00", DATE_FORMATTER), domain.getFechaInicio());
        assertEquals(LocalDateTime.parse("2026-12-31 23:59:59", DATE_FORMATTER), domain.getFechaFinal());
    }
}