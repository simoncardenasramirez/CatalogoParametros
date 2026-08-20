package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class ActualizarOrganizacionNombreIsNotNullRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarOrganizacionNombreIsNotNullRuleImpl rule;

    @Test
    void debePasarCuandoElNombreNoEsNulo() {
        var domain = ActualizarOrganizacionDomain.create(UUID.randomUUID(), "organizacion");
        assertDoesNotThrow(() -> rule.execute(domain));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-97"))
                .thenReturn("El nombre de la organizacion no puede ser nulo.");
        var domain = mock(ActualizarOrganizacionDomain.class);
        when(domain.getNombre()).thenReturn(null);
        assertThrows(ValidationException.class, () -> rule.execute(domain));
    }
}