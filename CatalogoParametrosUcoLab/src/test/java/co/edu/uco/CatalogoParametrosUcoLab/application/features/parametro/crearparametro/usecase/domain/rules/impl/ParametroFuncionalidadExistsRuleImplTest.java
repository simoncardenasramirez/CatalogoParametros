package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ParametroFuncionalidadExistsRuleImplTest {

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ParametroFuncionalidadExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    private CrearParametroDomain domainConIdFuncionalidad(final UUID idFuncionalidad) {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), "parametro",
                idFuncionalidad, UUID.randomUUID(), true);
    }

    private FuncionalidadEntity funcionalidadConId(final UUID id) {
        return FuncionalidadEntity.create(id, "funcionalidad", UUID.randomUUID(), true,
                LocalDateTime.now(), null);
    }

    @Test
    void debePasarCuandoLaFuncionalidadExisteConUnIdReal() {
        var idFuncionalidad = UUID.randomUUID();
        when(funcionalidadRepository.findById(idFuncionalidad))
                .thenReturn(Optional.of(funcionalidadConId(idFuncionalidad)));

        assertDoesNotThrow(() -> rule.execute(domainConIdFuncionalidad(idFuncionalidad)));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDomainEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-130"))
                .thenReturn("La funcionalidad asociada al parametro es obligatoria.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdFuncionalidadEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-130"))
                .thenReturn("La funcionalidad asociada al parametro es obligatoria.");

        assertThrows(ValidationException.class,
                () -> rule.execute(domainConIdFuncionalidad(UUIDHelper.getDefault())));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoNoExisteLaFuncionalidad() {
        var idFuncionalidad = UUID.randomUUID();
        when(funcionalidadRepository.findById(idFuncionalidad)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-129"))
                .thenReturn("La funcionalidad con el id no existe en el sistema.");

        assertThrows(NotFoundException.class,
                () -> rule.execute(domainConIdFuncionalidad(idFuncionalidad)));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaFuncionalidadTieneElIdPorDefecto() {
        var idFuncionalidad = UUID.randomUUID();
        when(funcionalidadRepository.findById(idFuncionalidad))
                .thenReturn(Optional.of(funcionalidadConId(UUIDHelper.getDefault())));
        when(consultarMensajePort.consultarMensaje("MSG-129"))
                .thenReturn("La funcionalidad con el id no existe en el sistema.");

        assertThrows(NotFoundException.class,
                () -> rule.execute(domainConIdFuncionalidad(idFuncionalidad)));
    }
}