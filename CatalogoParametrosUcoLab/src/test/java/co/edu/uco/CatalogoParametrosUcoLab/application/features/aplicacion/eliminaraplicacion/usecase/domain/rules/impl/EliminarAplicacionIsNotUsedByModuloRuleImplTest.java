package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class EliminarAplicacionIsNotUsedByModuloRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private EliminarAplicacionIsNotUsedByModuloRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    @Test
    void debePasarCuandoNingunModuloUsaLaAplicacion() {
        var id = UUID.randomUUID();
        when(moduloRepository.existsByIdAplicacion(id)).thenReturn(false);

        assertDoesNotThrow(() -> rule.execute(id));
    }

    @Test
    void debeLanzarConflictExceptionCuandoLaAplicacionEstaSiendoUsadaPorUnModulo() {
        var id = UUID.randomUUID();
        when(moduloRepository.existsByIdAplicacion(id)).thenReturn(true);
        when(consultarMensajePort.consultarMensaje("MSG-28"))
                .thenReturn("No se puede eliminar la aplicacion porque esta siendo usada por uno o mas modulos.");

        assertThrows(ConflictException.class, () -> rule.execute(id));
    }
}