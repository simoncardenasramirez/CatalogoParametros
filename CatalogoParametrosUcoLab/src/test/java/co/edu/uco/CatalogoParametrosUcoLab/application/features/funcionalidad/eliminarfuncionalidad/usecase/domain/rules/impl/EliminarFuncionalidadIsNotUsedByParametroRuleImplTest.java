package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
class EliminarFuncionalidadIsNotUsedByParametroRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private ParametroRepository parametroRepository;

    @InjectMocks
    private EliminarFuncionalidadIsNotUsedByParametroRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    @Test
    void debePasarCuandoNingunParametroUsaLaFuncionalidad() {
        var id = UUID.randomUUID();
        when(parametroRepository.findByIdFuncionalidad(id)).thenReturn(List.of());

        assertDoesNotThrow(() -> rule.execute(id));
        verify(parametroRepository).findByIdFuncionalidad(id);
    }

    @Test
    void debePasarCuandoLaConsultaDevuelveNulo() {
        var id = UUID.randomUUID();
        when(parametroRepository.findByIdFuncionalidad(id)).thenReturn(null);

        assertDoesNotThrow(() -> rule.execute(id));
    }

    @Test
    void debeLanzarConflictExceptionCuandoLaFuncionalidadEstaSiendoUsadaPorUnParametro() {
        var id = UUID.randomUUID();
        when(parametroRepository.findByIdFuncionalidad(id)).thenReturn(List.of(
                ParametroEntity.create(UUID.randomUUID(), "parametro", id, UUID.randomUUID(), true)));
        when(consultarMensajePort.consultarMensaje("MSG-59"))
                .thenReturn("No se puede eliminar la funcionalidad con el id porque esta siendo utilizada por uno o mas parametros.");

        assertThrows(ConflictException.class, () -> rule.execute(id));
    }
}