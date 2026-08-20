package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class ActualizarFuncionalidadNombreDoesNotExistRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @InjectMocks
    private ActualizarFuncionalidadNombreDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarFuncionalidadDomain domainConIdYNombre(final UUID id, final String nombre) {
        return ActualizarFuncionalidadDomain.create(id, nombre, UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoNoExisteLaFuncionalidadConElId() {
        var id = UUID.randomUUID();
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(id, "funcionalidad")));
    }

    @Test
    void debePasarCuandoElNombreNoCambiaAunqueExistaOtraConEseNombre() {
        var id = UUID.randomUUID();
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.of(
                FuncionalidadEntity.create(id, "funcionalidad", UUID.randomUUID(), true, null, null)));

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(id, "funcionalidad")));
    }

    @Test
    void debePasarCuandoElNombreCambiaPeroNoExisteOtraConEseNombre() {
        var id = UUID.randomUUID();
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.of(
                FuncionalidadEntity.create(id, "antiguo", UUID.randomUUID(), true, null, null)));
        when(funcionalidadRepository.existsByNombre("nuevo")).thenReturn(false);

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(id, "nuevo")));
    }

    @Test
    void debeLanzarConflictExceptionCuandoElNombreCambiaYYaExisteOtraConEseNombre() {
        var id = UUID.randomUUID();
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.of(
                FuncionalidadEntity.create(id, "antiguo", UUID.randomUUID(), true, null, null)));
        when(funcionalidadRepository.existsByNombre("nuevo")).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-42"))
                .thenReturn("Ya existe una funcionalidad con el nombre .");

        assertThrows(ConflictException.class, () -> rule.execute(domainConIdYNombre(id, "nuevo")));
    }
}