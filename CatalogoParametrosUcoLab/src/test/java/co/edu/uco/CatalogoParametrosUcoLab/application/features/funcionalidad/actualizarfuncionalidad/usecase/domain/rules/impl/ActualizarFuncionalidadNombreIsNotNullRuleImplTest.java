package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class ActualizarFuncionalidadNombreIsNotNullRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarFuncionalidadNombreIsNotNullRuleImpl rule;

    private ActualizarFuncionalidadDomain domainValido() {
        return ActualizarFuncionalidadDomain.create(UUID.randomUUID(), "funcionalidad", UUID.randomUUID(), true, null,
                null);
    }

    @Test
    void debePasarCuandoElDominioEsValido() {
        assertDoesNotThrow(() -> rule.execute(domainValido()));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDominioEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-44"))
                .thenReturn("El nombre de la funcionalidad es obligatorio.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }
}