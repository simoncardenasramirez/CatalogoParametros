package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ActualizarAplicacionIdExistsRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private AplicacionRepository aplicacionRepository;

    @InjectMocks
    private ActualizarAplicacionIdExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarAplicacionDomain domainConId(final UUID id) {
        return ActualizarAplicacionDomain.create(id, "aplicacion", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoLaAplicacionExisteConUnIdValido() {
        var id = UUID.randomUUID();
        when(aplicacionRepository.findById(id))
                .thenReturn(Optional.of(AplicacionEntity.create(id, "aplicacion", UUID.randomUUID(), true, null, null)));

        assertDoesNotThrow(() -> rule.execute(domainConId(id)));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-11"))
                .thenReturn("El id de la aplicacion es obligatorio para actualizar.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConId(UUIDHelper.getDefault())));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDominioEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-11"))
                .thenReturn("El id de la aplicacion es obligatorio para actualizar.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaAplicacionNoExiste() {
        var id = UUID.randomUUID();
        when(aplicacionRepository.findById(id)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-10"))
                .thenReturn("No existe una aplicacion con el id especificado.");

        assertThrows(NotFoundException.class, () -> rule.execute(domainConId(id)));
    }
}