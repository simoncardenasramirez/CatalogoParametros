package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class ActualizarAplicacionNombreDoesNotExistRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private AplicacionRepository aplicacionRepository;

    @InjectMocks
    private ActualizarAplicacionNombreDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarAplicacionDomain domainConIdYNombre(final UUID id, final String nombre) {
        return ActualizarAplicacionDomain.create(id, nombre, UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoNoExisteLaAplicacionConElId() {
        var id = UUID.randomUUID();
        when(aplicacionRepository.findById(id)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(id, "aplicacion")));
    }

    @Test
    void debePasarCuandoElNombreNoCambiaAunqueExistaOtroConEseNombre() {
        var id = UUID.randomUUID();
        when(aplicacionRepository.findById(id))
                .thenReturn(Optional.of(AplicacionEntity.create(id, "aplicacion", UUID.randomUUID(), true, null, null)));

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(id, "aplicacion")));
    }

    @Test
    void debePasarCuandoElNombreCambiaPeroNoExisteOtroConEseNombre() {
        var id = UUID.randomUUID();
        when(aplicacionRepository.findById(id))
                .thenReturn(Optional.of(AplicacionEntity.create(id, "antiguo", UUID.randomUUID(), true, null, null)));
        when(aplicacionRepository.existsByNombre("nuevo")).thenReturn(false);

        assertDoesNotThrow(() -> rule.execute(domainConIdYNombre(id, "nuevo")));
    }

    @Test
    void debeLanzarConflictExceptionCuandoElNombreCambiaYYaExisteOtroConEseNombre() {
        var id = UUID.randomUUID();
        when(aplicacionRepository.findById(id))
                .thenReturn(Optional.of(AplicacionEntity.create(id, "antiguo", UUID.randomUUID(), true, null, null)));
        when(aplicacionRepository.existsByNombre("nuevo")).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-12"))
                .thenReturn("Ya existe una aplicacion con el nombre .");

        assertThrows(ConflictException.class, () -> rule.execute(domainConIdYNombre(id, "nuevo")));
    }
}