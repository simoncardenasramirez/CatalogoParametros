package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ActualizarModuloNombreDoesNotExistRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private ActualizarModuloNombreDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarModuloDomain domainConIdYNombre(final UUID id, final String nombre) {
        return ActualizarModuloDomain.create(id, nombre, UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoNoExisteUnModuloConElNombre() {
        when(moduloRepository.existsByNombre("modulo")).thenReturn(false);

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(UUID.randomUUID(), "modulo")));
    }

    @Test
    void debeLanzarConflictExceptionCuandoYaExisteUnModuloConElNombre() {
        when(moduloRepository.existsByNombre("modulo")).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-75")).thenReturn("El nombre del modulo ya existe en el sistema.");

        assertThrows(ConflictException.class,
                () -> rule.execute(domainConIdYNombre(UUID.randomUUID(), "modulo")));
    }

    @Test
    void debeNoLanzarCuandoElNombreExistePeroElIdEsElPorDefecto() {
        when(moduloRepository.existsByNombre("modulo")).thenReturn(true);

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(UUIDHelper.getDefault(), "modulo")));
    }
}