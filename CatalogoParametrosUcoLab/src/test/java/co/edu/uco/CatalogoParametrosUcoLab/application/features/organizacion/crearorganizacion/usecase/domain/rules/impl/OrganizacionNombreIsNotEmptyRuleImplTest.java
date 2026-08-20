package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class OrganizacionNombreIsNotEmptyRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private OrganizacionNombreIsNotEmptyRuleImpl rule;

    @Test
    void debePasarCuandoElNombreNoEstaVacio() {
        var domain = CrearOrganizacionDomain.create(UUID.randomUUID(), "organizacion valida");
        assertDoesNotThrow(() -> rule.execute(domain));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        when(consultarMensajePort.consultarMensaje("MSG-102"))
                .thenReturn("El nombre de la organizacion no puede estar vacio.");
        var domain = CrearOrganizacionDomain.create(UUID.randomUUID(), "   ");
        assertThrows(ValidationException.class, () -> rule.execute(domain));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaDataEsNula() {
        when(consultarMensajePort.consultarMensaje("MSG-102"))
                .thenReturn("El nombre de la organizacion no puede estar vacio.");
        assertThrows(ValidationException.class, () -> rule.execute(null));
    }
}