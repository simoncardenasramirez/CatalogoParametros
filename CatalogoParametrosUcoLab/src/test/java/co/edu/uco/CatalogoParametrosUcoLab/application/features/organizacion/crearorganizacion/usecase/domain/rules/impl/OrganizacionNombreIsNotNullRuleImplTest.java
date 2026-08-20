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
class OrganizacionNombreIsNotNullRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private OrganizacionNombreIsNotNullRuleImpl rule;

    @Test
    void debePasarCuandoLaDataNoEsNula() {
        var domain = CrearOrganizacionDomain.create(UUID.randomUUID(), "organizacion");
        assertDoesNotThrow(() -> rule.execute(domain));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaDataEsNula() {
        when(consultarMensajePort.consultarMensaje("MSG-103"))
                .thenReturn("El nombre de la organizacion es obligatorio.");
        assertThrows(ValidationException.class, () -> rule.execute(null));
    }
}