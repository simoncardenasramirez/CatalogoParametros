package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.crearmoduloimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModuloRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.event.CrearModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.publisher.CrearModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class CrearModuloImplTest {

    @Mock
    private ModuloRepository moduloRepository;

    @Mock
    private CrearModuloPublisher crearModuloPublisher;

    @Mock
    private CrearModuloRuleValidator crearModuloRuleValidator;

    @InjectMocks
    private CrearModuloImpl useCase;

    private CrearModuloDomain domainValido() {
        return CrearModuloDomain.create(UUIDHelper.getDefault(), "modulo", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debeCrearModuloExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        when(moduloRepository.save(any(ModuloEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(crearModuloRuleValidator).validate(domain);
        verify(moduloRepository).save(any(ModuloEntity.class));
        verify(crearModuloPublisher).sendEvent(any(CrearModuloEvent.class));
    }

    @Test
    void debeFallarCuandoLaValidacionFalla() {
        var domain = domainValido();
        doThrow(ValidationException.build("error")).when(crearModuloRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));

        verify(moduloRepository, never()).save(any());
        verify(crearModuloPublisher, never()).sendEvent(any());
    }
}