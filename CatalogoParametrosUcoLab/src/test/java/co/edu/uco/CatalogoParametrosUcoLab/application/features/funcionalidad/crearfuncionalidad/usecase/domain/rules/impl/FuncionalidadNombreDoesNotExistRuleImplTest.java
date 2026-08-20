package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class FuncionalidadNombreDoesNotExistRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @InjectMocks
    private FuncionalidadNombreDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private CrearFuncionalidadDomain domainConNombre(final String nombre) {
        return CrearFuncionalidadDomain.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoNoExisteUnaFuncionalidadConEseNombre() {
        when(funcionalidadRepository.existsByNombre("funcionalidad")).thenReturn(false);

        assertDoesNotThrow(() -> rule.execute(domainConNombre("funcionalidad")));
        verify(funcionalidadRepository).existsByNombre("funcionalidad");
    }

    @Test
    void debeLanzarConflictExceptionCuandoYaExisteUnaFuncionalidadConEseNombre() {
        when(funcionalidadRepository.existsByNombre("funcionalidad")).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-54"))
                .thenReturn("Ya existe una funcionalidad con el nombre .");

        assertThrows(ConflictException.class, () -> rule.execute(domainConNombre("funcionalidad")));
    }
}