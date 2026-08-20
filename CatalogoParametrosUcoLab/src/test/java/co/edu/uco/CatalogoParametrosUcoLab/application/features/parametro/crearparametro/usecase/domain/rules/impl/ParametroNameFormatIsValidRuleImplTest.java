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
class ParametroNameFormatIsValidRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ParametroNameFormatIsValidRuleImpl rule;

    private CrearParametroDomain domainConNombre(final String nombre) {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), nombre,
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debePasarCuandoElNombreTieneFormatoValido() {
        assertDoesNotThrow(() -> rule.execute(domainConNombre("parametro-1")));
        assertDoesNotThrow(() -> rule.execute(domainConNombre("parametro_uno")));
        assertDoesNotThrow(() -> rule.execute(domainConNombre("parametro.uno")));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneCaracteresInvalidos() {
        when(consultarMensajePort.consultarMensaje("MSG-133"))
                .thenReturn("El nombre del parametro solo puede contener letras, numeros, guion, punto y guion bajo.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConNombre("parametro con espacios")));
        assertThrows(ValidationException.class, () -> rule.execute(domainConNombre("parametro#")));
    }
}