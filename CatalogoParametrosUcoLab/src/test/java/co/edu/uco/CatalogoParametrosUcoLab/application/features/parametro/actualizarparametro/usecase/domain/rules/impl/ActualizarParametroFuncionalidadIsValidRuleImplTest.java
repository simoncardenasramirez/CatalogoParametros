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
class ActualizarParametroFuncionalidadIsValidRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ActualizarParametroFuncionalidadIsValidRuleImpl rule;

    private ActualizarParametroDomain domainConIdFuncionalidad(final UUID idFuncionalidad) {
        return ActualizarParametroDomain.create(UUID.randomUUID(), "parametro",
                idFuncionalidad, UUID.randomUUID(), true);
    }

    @Test
    void debePasarCuandoElIdFuncionalidadEsReal() {
        assertDoesNotThrow(() -> rule.execute(domainConIdFuncionalidad(UUID.randomUUID())));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDomainEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-118"))
                .thenReturn("La funcionalidad asociada al parametro es obligatoria.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdFuncionalidadEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-118"))
                .thenReturn("La funcionalidad asociada al parametro es obligatoria.");

        assertThrows(ValidationException.class,
                () -> rule.execute(domainConIdFuncionalidad(UUIDHelper.getDefault())));
    }
}