package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class OrganizacionNombreDoesNotExistRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private OrganizacionRepository organizacionRepository;

    @InjectMocks
    private OrganizacionNombreDoesNotExistRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private CrearOrganizacionDomain domainConNombre(final String nombre) {
        return CrearOrganizacionDomain.create(UUID.randomUUID(), nombre);
    }

    @Test
    void debePasarCuandoNoExisteOtraOrganizacionConElNombre() {
        when(organizacionRepository.existsByNombre("organizacion")).thenReturn(false);
        assertDoesNotThrow(() -> rule.execute(domainConNombre("organizacion")));
    }

    @Test
    void debeLanzarConflictExceptionCuandoYaExisteUnaOrganizacionConElNombre() {
        when(organizacionRepository.existsByNombre("organizacion")).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-101"))
                .thenReturn("Ya existe una organizacion con el nombre");
        assertThrows(ConflictException.class, () -> rule.execute(domainConNombre("organizacion")));
    }
}