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
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ActualizarAplicacionOrganizacionExistsRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private OrganizacionRepository organizacionRepository;

    @InjectMocks
    private ActualizarAplicacionOrganizacionExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarAplicacionDomain domainConIdOrganizacion(final UUID idOrganizacion) {
        return ActualizarAplicacionDomain.create(UUID.randomUUID(), "aplicacion", idOrganizacion, true, null, null);
    }

    @Test
    void debePasarCuandoLaOrganizacionExiste() {
        var idOrganizacion = UUID.randomUUID();
        when(organizacionRepository.findById(idOrganizacion))
                .thenReturn(Optional.of(OrganizacionEntity.create(idOrganizacion, "organizacion")));

        assertDoesNotThrow(() -> rule.execute(domainConIdOrganizacion(idOrganizacion)));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaOrganizacionNoExiste() {
        var idOrganizacion = UUID.randomUUID();
        when(organizacionRepository.findById(idOrganizacion)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-15"))
                .thenReturn("La organizacion con id no existe.");

        assertThrows(NotFoundException.class, () -> rule.execute(domainConIdOrganizacion(idOrganizacion)));
    }
}