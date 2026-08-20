package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class EliminarOrganizacionIsNotUsedByAplicacionRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private AplicacionRepository aplicacionRepository;

    @InjectMocks
    private EliminarOrganizacionIsNotUsedByAplicacionRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    @Test
    void debePasarCuandoLaOrganizacionNoEstaSiendoUsadaPorAplicaciones() {
        when(aplicacionRepository.existsByIdOrganizacion(any(UUID.class))).thenReturn(false);
        assertDoesNotThrow(() -> rule.execute(UUID.randomUUID()));
    }

    @Test
    void debeLanzarConflictExceptionCuandoLaOrganizacionEstaSiendoUsada() {
        when(aplicacionRepository.existsByIdOrganizacion(any(UUID.class))).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-105")).thenReturn(
                "No se puede eliminar la organizacion porque esta siendo usada por una o mas aplicaciones.");
        assertThrows(ConflictException.class, () -> rule.execute(UUID.randomUUID()));
    }
}