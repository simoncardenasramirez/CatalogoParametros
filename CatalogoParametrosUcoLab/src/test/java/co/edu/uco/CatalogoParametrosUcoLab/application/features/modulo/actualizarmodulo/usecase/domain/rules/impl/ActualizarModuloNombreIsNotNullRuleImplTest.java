package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

@ExtendWith(MockitoExtension.class)
class ActualizarModuloNombreIsNotNullRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarModuloNombreIsNotNullRuleImpl rule;

    @Test
    void debePasarCuandoElNombreNoEsNulo() {
        var domain = ActualizarModuloDomain.create(UUID.randomUUID(), "modulo", UUID.randomUUID(), true, null, null);

        assertDoesNotThrow(() -> rule.execute(domain));
    }
}