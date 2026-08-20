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
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ActualizarParametroTipoParametroIsValidRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarParametroTipoParametroIsValidRuleImpl rule;

    private ActualizarParametroDomain domainConIdTipoParametro(final UUID idTipoParametro) {
        return ActualizarParametroDomain.create(UUID.randomUUID(), "parametro",
                UUID.randomUUID(), idTipoParametro, true);
    }

    @Test
    void debePasarCuandoElIdTipoParametroEsReal() {
        assertDoesNotThrow(() -> rule.execute(domainConIdTipoParametro(UUID.randomUUID())));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDomainEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-121"))
                .thenReturn("El tipo de parametro asociado es obligatorio.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdTipoParametroEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-121"))
                .thenReturn("El tipo de parametro asociado es obligatorio.");

        assertThrows(ValidationException.class,
                () -> rule.execute(domainConIdTipoParametro(UUIDHelper.getDefault())));
    }
}