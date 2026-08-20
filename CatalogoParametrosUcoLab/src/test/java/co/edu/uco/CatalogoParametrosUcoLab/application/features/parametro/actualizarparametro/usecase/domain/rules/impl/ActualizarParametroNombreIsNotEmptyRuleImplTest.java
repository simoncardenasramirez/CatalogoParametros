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
class ActualizarParametroNombreIsNotEmptyRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarParametroNombreIsNotEmptyRuleImpl rule;

    private ActualizarParametroDomain domainConNombre(final String nombre) {
        return ActualizarParametroDomain.create(UUID.randomUUID(), nombre,
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debePasarCuandoElNombreNoEstaVacio() {
        assertDoesNotThrow(() -> rule.execute(domainConNombre("parametro valido")));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        when(consultarMensajePort.consultarMensaje("MSG-119"))
                .thenReturn("El nombre del parametro no puede estar vacio.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConNombre("   ")));
    }
}