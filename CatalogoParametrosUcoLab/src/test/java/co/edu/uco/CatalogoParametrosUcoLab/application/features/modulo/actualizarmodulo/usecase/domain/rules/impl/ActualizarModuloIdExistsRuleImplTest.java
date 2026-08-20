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
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ActualizarModuloIdExistsRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private ActualizarModuloIdExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarModuloDomain domainConId(final UUID id) {
        return ActualizarModuloDomain.create(id, "modulo", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debePasarCuandoElModuloExiste() {
        var id = UUID.randomUUID();
        when(moduloRepository.findById(id))
                .thenReturn(Optional.of(ModuloEntity.create(id, "modulo", UUID.randomUUID(), true, null, null)));

        assertDoesNotThrow(() -> rule.execute(domainConId(id)));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-74"))
                .thenReturn("El id del modulo es obligatorio para actualizar.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConId(UUIDHelper.getDefault())));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoElModuloNoExiste() {
        var id = UUID.randomUUID();
        when(moduloRepository.findById(id)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-73"))
                .thenReturn("No existe un modulo con el id especificado.");

        assertThrows(NotFoundException.class, () -> rule.execute(domainConId(id)));
    }
}