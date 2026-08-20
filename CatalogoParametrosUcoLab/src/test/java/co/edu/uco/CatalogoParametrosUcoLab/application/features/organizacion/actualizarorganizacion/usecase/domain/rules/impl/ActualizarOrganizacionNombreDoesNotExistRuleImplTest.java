package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class ActualizarOrganizacionNombreDoesNotExistRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private OrganizacionRepository organizacionRepository;

    @InjectMocks
    private ActualizarOrganizacionNombreDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    @Test
    void debePasarCuandoNoExisteOtraOrganizacionConElMismoNombre() {
        when(organizacionRepository.findAll()).thenReturn(List.of());
        var domain = ActualizarOrganizacionDomain.create(UUID.randomUUID(), "organizacion");
        assertDoesNotThrow(() -> rule.execute(domain));
    }

    @Test
    void debePasarCuandoLaOrganizacionExistenteEsLaMismaPorId() {
        var id = UUID.randomUUID();
        var existente = OrganizacionEntity.create(id, "Organizacion");
        when(organizacionRepository.findAll()).thenReturn(List.of(existente));
        var domain = ActualizarOrganizacionDomain.create(id, "organizacion");
        assertDoesNotThrow(() -> rule.execute(domain));
    }

    @Test
    void debeLanzarConflictExceptionCuandoOtraOrganizacionTieneElMismoNombre() {
        var existente = OrganizacionEntity.create(UUID.randomUUID(), "Organizacion");
        when(organizacionRepository.findAll()).thenReturn(List.of(existente));
        when(consultarMensajePort.consultarMensaje("MSG-95"))
                .thenReturn("Ya existe una organizacion con el nombre:");
        var domain = ActualizarOrganizacionDomain.create(UUID.randomUUID(), "organizacion");
        assertThrows(ConflictException.class, () -> rule.execute(domain));
    }
}