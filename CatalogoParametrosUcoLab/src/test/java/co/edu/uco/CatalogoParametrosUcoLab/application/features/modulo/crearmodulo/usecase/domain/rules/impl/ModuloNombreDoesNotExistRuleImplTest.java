package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class ModuloNombreDoesNotExistRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private ModuloNombreDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private CrearModuloDomain domainConNombre(final String nombre) {
        return CrearModuloDomain.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoNoExisteUnModuloConElNombre() {
        when(moduloRepository.existsByNombre("modulo")).thenReturn(false);

        assertDoesNotThrow(() -> rule.execute(domainConNombre("modulo")));
    }

    @Test
    void debeLanzarConflictExceptionCuandoYaExisteUnModuloConElNombre() {
        when(moduloRepository.existsByNombre("modulo")).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-86")).thenReturn("Ya existe un modulo con el nombre .");

        assertThrows(ConflictException.class, () -> rule.execute(domainConNombre("modulo")));
    }
}