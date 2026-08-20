package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.impl;

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

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ActualizarFuncionalidadIdExistsRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @InjectMocks
    private ActualizarFuncionalidadIdExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarFuncionalidadDomain domainConId(final UUID id) {
        return ActualizarFuncionalidadDomain.create(id, "funcionalidad", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoLaFuncionalidadExisteConUnIdValido() {
        var id = UUID.randomUUID();
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.of(
                FuncionalidadEntity.create(id, "funcionalidad", UUID.randomUUID(), true, null, null)));

        assertDoesNotThrow(() -> rule.execute(domainConId(id)));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-39"))
                .thenReturn("El id de la funcionalidad es obligatorio para actualizar.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConId(UUIDHelper.getDefault())));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDominioEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-39"))
                .thenReturn("El id de la funcionalidad es obligatorio para actualizar.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaFuncionalidadNoExiste() {
        var id = UUID.randomUUID();
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-38"))
                .thenReturn("No existe una funcionalidad con el id especificado.");

        assertThrows(NotFoundException.class, () -> rule.execute(domainConId(id)));
    }
}