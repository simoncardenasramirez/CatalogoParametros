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
class ActualizarFuncionalidadNombreIsNotEmptyRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarFuncionalidadNombreIsNotEmptyRuleImpl rule;

    private ActualizarFuncionalidadDomain domainConNombre(final String nombre) {
        return ActualizarFuncionalidadDomain.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoElNombreNoEstaVacio() {
        assertDoesNotThrow(() -> rule.execute(domainConNombre("funcionalidad")));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        when(consultarMensajePort.consultarMensaje("MSG-43"))
                .thenReturn("El nombre de la funcionalidad no puede estar vacío.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConNombre("  ")));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDominioEsNulo() {
        assertThrows(ValidationException.class, () -> rule.execute(null));
    }
}