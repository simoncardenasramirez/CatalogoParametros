package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class ActualizarParametroNombreDoesNotExistRuleImplTest {

    @Mock
    private ParametroRepository parametroRepository;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarParametroNombreDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarParametroDomain domainConIdYNombre(final UUID id, final String nombre) {
        return ActualizarParametroDomain.create(id, nombre, UUID.randomUUID(), UUID.randomUUID(), true);
    }

    private ParametroEntity entidadConIdYNombre(final UUID id, final String nombre) {
        return ParametroEntity.create(id, nombre, UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debePasarCuandoNoExisteOtroParametroConElMismoNombre() {
        var id = UUID.randomUUID();
        when(parametroRepository.findAll()).thenReturn(List.of());

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(id, "parametro")));
    }

    @Test
    void debePasarCuandoElParametroConElMismoNombreEsElMismoRegistro() {
        var id = UUID.randomUUID();
        when(parametroRepository.findAll())
                .thenReturn(List.of(entidadConIdYNombre(id, "PARAMETRO")));

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(id, "parametro")));
    }

    @Test
    void debeLanzarConflictExceptionCuandoOtroParametroTieneElMismoNombreIgnorandoMayusculas() {
        var id = UUID.randomUUID();
        when(parametroRepository.findAll())
                .thenReturn(List.of(entidadConIdYNombre(UUID.randomUUID(), "PARAMETRO")));
        when(consultarMensajePort.consultarMensaje("MSG-132"))
                .thenReturn("Ya existe un parametro con ese nombre.");

        assertThrows(ConflictException.class, () -> rule.execute(domainConIdYNombre(id, "parametro")));
    }
}