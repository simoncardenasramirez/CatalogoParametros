package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class EliminarOrganizacionIdExistsRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private OrganizacionRepository organizacionRepository;

    @InjectMocks
    private EliminarOrganizacionIdExistsRuleImpl rule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rule, "consultarMensajePort", consultarMensajePort);
    }

    @Test
    void debePasarCuandoExisteLaOrganizacionConElId() {
        var id = UUID.randomUUID();
        var existente = OrganizacionEntity.create(id, "organizacion");
        when(organizacionRepository.findById(id)).thenReturn(Optional.of(existente));
        assertDoesNotThrow(() -> rule.execute(id));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoNoExisteLaOrganizacionConElId() {
        when(organizacionRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-104"))
                .thenReturn("La organizacion con id no existe.");
        assertThrows(NotFoundException.class, () -> rule.execute(UUID.randomUUID()));
    }
}