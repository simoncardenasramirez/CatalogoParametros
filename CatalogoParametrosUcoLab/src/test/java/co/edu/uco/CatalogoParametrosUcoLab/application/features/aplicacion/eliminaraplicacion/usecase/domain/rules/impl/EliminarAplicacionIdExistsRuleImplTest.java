package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class EliminarAplicacionIdExistsRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private AplicacionRepository aplicacionRepository;

    @InjectMocks
    private EliminarAplicacionIdExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    @Test
    void debePasarCuandoLaAplicacionExiste() {
        var id = UUID.randomUUID();
        when(aplicacionRepository.findById(id))
                .thenReturn(Optional.of(AplicacionEntity.create(id, "aplicacion", UUID.randomUUID(), true, null, null)));

        assertDoesNotThrow(() -> rule.execute(id));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaAplicacionNoExiste() {
        var id = UUID.randomUUID();
        when(aplicacionRepository.findById(id)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-27"))
                .thenReturn("La aplicacion con id no existe.");

        assertThrows(NotFoundException.class, () -> rule.execute(id));
    }
}