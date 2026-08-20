package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class AplicacionNombreDoesNotExistRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private AplicacionRepository aplicacionRepository;

    @InjectMocks
    private AplicacionNombreDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private CrearAplicacionDomain domainConNombre(final String nombre) {
        return CrearAplicacionDomain.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoNoExisteUnaAplicacionConEseNombre() {
        when(aplicacionRepository.existsByNombre("aplicacion")).thenReturn(false);

        assertDoesNotThrow(() -> rule.execute(domainConNombre("aplicacion")));
        verify(aplicacionRepository).existsByNombre("aplicacion");
    }

    @Test
    void debeLanzarConflictExceptionCuandoYaExisteUnaAplicacionConEseNombre() {
        when(aplicacionRepository.existsByNombre("aplicacion")).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-23"))
                .thenReturn("Ya existe una aplicacion con el nombre .");

        assertThrows(ConflictException.class, () -> rule.execute(domainConNombre("aplicacion")));
    }
}