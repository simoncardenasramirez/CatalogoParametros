package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class ActualizarParametroNombreIsNotNullRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarParametroNombreIsNotNullRuleImpl rule;

    private ActualizarParametroDomain domainValido() {
        return ActualizarParametroDomain.create(UUID.randomUUID(), "parametro",
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debePasarCuandoElDomainNoEsNuloYElNombreNoEsNulo() {
        assertDoesNotThrow(() -> rule.execute(domainValido()));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDomainEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-120"))
                .thenReturn("El nombre del parametro es obligatorio.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }
}