package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class FuncionalidadNombreIsNotEmptyRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private FuncionalidadNombreIsNotEmptyRuleImpl rule;

    private CrearFuncionalidadDomain domainConNombre(final String nombre) {
        return CrearFuncionalidadDomain.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoElNombreNoEstaVacio() {
        assertDoesNotThrow(() -> rule.execute(domainConNombre("funcionalidad")));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        when(consultarMensajePort.consultarMensaje("MSG-55"))
                .thenReturn("El nombre de la funcionalidad no puede estar vacio.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConNombre("  ")));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDominioEsNulo() {
        assertThrows(ValidationException.class, () -> rule.execute(null));
    }
}