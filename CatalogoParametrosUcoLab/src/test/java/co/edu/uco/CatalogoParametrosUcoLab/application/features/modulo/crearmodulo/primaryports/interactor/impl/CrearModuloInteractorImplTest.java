package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;

@ExtendWith(MockitoExtension.class)
class CrearModuloInteractorImplTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mock
    private CrearModulo crearModulo;

    @InjectMocks
    private CrearModuloInteractorImpl interactor;

    @Test
    void debeDelegarEnElUseCaseConElDominioMapeado() {
        var idAplicacion = UUID.randomUUID().toString();
        var request = CrearModuloDtoRequest.create("modulo", idAplicacion, "true",
                "2026-01-01 00:00:00", "2026-12-31 23:59:59");

        interactor.execute(request);

        ArgumentCaptor<CrearModuloDomain> captor = ArgumentCaptor.forClass(CrearModuloDomain.class);
        verify(crearModulo).execute(captor.capture());
        var domain = captor.getValue();
        assertEquals("modulo", domain.getNombre());
        assertEquals(UUID.fromString(idAplicacion), domain.getIdAplicacion());
        assertTrue(domain.isActivo());
        assertEquals(LocalDateTime.parse("2026-01-01 00:00:00", DATE_FORMATTER), domain.getFechaInicio());
        assertEquals(LocalDateTime.parse("2026-12-31 23:59:59", DATE_FORMATTER), domain.getFechaFinal());
    }
}