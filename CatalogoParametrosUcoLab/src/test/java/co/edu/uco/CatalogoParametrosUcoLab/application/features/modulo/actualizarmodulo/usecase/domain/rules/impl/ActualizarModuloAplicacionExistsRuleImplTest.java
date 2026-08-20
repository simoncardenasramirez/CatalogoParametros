package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ActualizarModuloAplicacionExistsRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private AplicacionRepository aplicacionRepository;

    @InjectMocks
    private ActualizarModuloAplicacionExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarModuloDomain domainConIdAplicacion(final UUID idAplicacion) {
        return ActualizarModuloDomain.create(UUID.randomUUID(), "modulo", idAplicacion, true, null, null);
    }

    @Test
    void debePasarCuandoLaAplicacionExiste() {
        var idAplicacion = UUID.randomUUID();
        when(aplicacionRepository.findById(idAplicacion))
                .thenReturn(Optional.of(AplicacionEntity.create(idAplicacion, "aplicacion",
                        UUID.randomUUID(), true, null, null)));

        assertDoesNotThrow(() -> rule.execute(domainConIdAplicacion(idAplicacion)));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoElIdAplicacionEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-72"))
                .thenReturn("La aplicacion asociada al modulo es obligatoria.");

        assertThrows(NotFoundException.class, () -> rule.execute(domainConIdAplicacion(UUIDHelper.getDefault())));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaAplicacionNoExiste() {
        var idAplicacion = UUID.randomUUID();
        when(aplicacionRepository.findById(idAplicacion)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-71"))
                .thenReturn("La aplicacion asociada al modulo no existe.");

        assertThrows(NotFoundException.class, () -> rule.execute(domainConIdAplicacion(idAplicacion)));
    }
}