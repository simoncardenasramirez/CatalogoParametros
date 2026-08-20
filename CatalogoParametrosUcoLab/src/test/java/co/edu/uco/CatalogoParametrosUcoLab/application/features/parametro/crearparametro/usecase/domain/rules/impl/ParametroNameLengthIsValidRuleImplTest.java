package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ParametroNameLengthIsValidRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ParametroNameLengthIsValidRuleImpl rule;

    private CrearParametroDomain domainConNombre(final String nombre) {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), nombre,
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debePasarCuandoElNombreTieneLongitudValida() {
        assertDoesNotThrow(() -> rule.execute(domainConNombre("parametro")));
        assertDoesNotThrow(() -> rule.execute(domainConNombre("a".repeat(3))));
        assertDoesNotThrow(() -> rule.execute(domainConNombre("a".repeat(120))));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEsDemasiadoCorto() {
        when(consultarMensajePort.consultarMensaje("MSG-136"))
                .thenReturn("El nombre del parametro debe tener entre 3 y 120 caracteres.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConNombre("ab")));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEsDemasiadoLargo() {
        when(consultarMensajePort.consultarMensaje("MSG-136"))
                .thenReturn("El nombre del parametro debe tener entre 3 y 120 caracteres.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConNombre("a".repeat(121))));
    }
}