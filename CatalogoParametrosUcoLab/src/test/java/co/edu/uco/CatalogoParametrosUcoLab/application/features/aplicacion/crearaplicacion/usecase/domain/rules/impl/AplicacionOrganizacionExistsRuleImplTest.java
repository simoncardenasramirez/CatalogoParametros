package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class AplicacionOrganizacionExistsRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private OrganizacionRepository organizacionRepository;

    @InjectMocks
    private AplicacionOrganizacionExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private CrearAplicacionDomain domainConIdOrganizacion(final UUID idOrganizacion) {
        return CrearAplicacionDomain.create(UUID.randomUUID(), "aplicacion", idOrganizacion, true, null, null);
    }

    @Test
    void debePasarCuandoLaOrganizacionExiste() {
        var idOrganizacion = UUID.randomUUID();
        when(organizacionRepository.findById(idOrganizacion))
                .thenReturn(Optional.of(OrganizacionEntity.create(idOrganizacion, "organizacion")));

        assertDoesNotThrow(() -> rule.execute(domainConIdOrganizacion(idOrganizacion)));
        verify(organizacionRepository).findById(idOrganizacion);
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaOrganizacionNoExiste() {
        var idOrganizacion = UUID.randomUUID();
        when(organizacionRepository.findById(idOrganizacion)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-26"))
                .thenReturn("La organizacion con id no existe.");

        assertThrows(NotFoundException.class, () -> rule.execute(domainConIdOrganizacion(idOrganizacion)));
    }
}