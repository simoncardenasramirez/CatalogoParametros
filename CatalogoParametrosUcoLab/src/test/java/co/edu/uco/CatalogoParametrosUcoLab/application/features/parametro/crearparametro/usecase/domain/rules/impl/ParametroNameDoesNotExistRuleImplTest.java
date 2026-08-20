package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ParametroNameDoesNotExistRuleImplTest {

    @Mock
    private ParametroRepository parametroRepository;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ParametroNameDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private CrearParametroDomain domainValido() {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), "parametro",
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debePasarCuandoNoExisteUnParametroConElMismoNombre() {
        when(parametroRepository.existsByNombre("parametro")).thenReturn(false);

        assertDoesNotThrow(() -> rule.execute(domainValido()));

        verify(parametroRepository).existsByNombre("parametro");
    }

    @Test
    void debeLanzarConflictExceptionCuandoYaExisteUnParametroConElMismoNombre() {
        when(parametroRepository.existsByNombre("parametro")).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-132"))
                .thenReturn("Ya existe un parametro con ese nombre.");

        assertThrows(ConflictException.class, () -> rule.execute(domainValido()));
    }

    @Test
    void debeFallarSinConsultarElMensajeCuandoNoExisteElParametro() {
        when(parametroRepository.existsByNombre("parametro")).thenReturn(false);

        assertDoesNotThrow(() -> rule.execute(domainValido()));

        verify(consultarMensajePort, never()).consultarMensaje("MSG-132");
    }
}