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
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ActualizarFuncionalidadModuloExistsRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private ActualizarFuncionalidadModuloExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private ActualizarFuncionalidadDomain domainConIdModulo(final UUID idModulo) {
        return ActualizarFuncionalidadDomain.create(UUID.randomUUID(), "funcionalidad", idModulo, true, null, null);
    }

    @Test
    void debePasarCuandoElModuloExiste() {
        var idModulo = UUID.randomUUID();
        when(moduloRepository.findById(idModulo))
                .thenReturn(Optional.of(ModuloEntity.create(idModulo, "modulo", UUID.randomUUID(), true, null, null)));

        assertDoesNotThrow(() -> rule.execute(domainConIdModulo(idModulo)));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdModuloEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-41"))
                .thenReturn("El modulo asociado a la funcionalidad es obligatorio.");

        assertThrows(ValidationException.class, () -> rule.execute(domainConIdModulo(UUIDHelper.getDefault())));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDominioEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-41"))
                .thenReturn("El modulo asociado a la funcionalidad es obligatorio.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoElModuloNoExiste() {
        var idModulo = UUID.randomUUID();
        when(moduloRepository.findById(idModulo)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-40"))
                .thenReturn("El modulo con el id no existe en el sistema.");

        assertThrows(NotFoundException.class, () -> rule.execute(domainConIdModulo(idModulo)));
    }
}