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
class ParametroFuncionalidadIsValidRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ParametroFuncionalidadIsValidRuleImpl rule;

    private CrearParametroDomain domainConIdFuncionalidad(final UUID idFuncionalidad) {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), "parametro",
                idFuncionalidad, UUID.randomUUID(), true);
    }

    @Test
    void debePasarCuandoElIdFuncionalidadEsReal() {
        assertDoesNotThrow(() -> rule.execute(domainConIdFuncionalidad(UUID.randomUUID())));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDomainEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-131"))
                .thenReturn("La funcionalidad asociada al parametro es obligatoria.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdFuncionalidadEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-131"))
                .thenReturn("La funcionalidad asociada al parametro es obligatoria.");

        assertThrows(ValidationException.class,
                () -> rule.execute(domainConIdFuncionalidad(UUIDHelper.getDefault())));
    }
}