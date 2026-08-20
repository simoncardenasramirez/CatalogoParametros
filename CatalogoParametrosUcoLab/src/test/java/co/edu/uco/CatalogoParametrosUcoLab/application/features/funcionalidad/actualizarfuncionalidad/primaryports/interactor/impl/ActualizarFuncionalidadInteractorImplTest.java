package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class ActualizarFuncionalidadInteractorImplTest {

    @Mock
    private ActualizarFuncionalidad actualizarFuncionalidad;

    @InjectMocks
    private ActualizarFuncionalidadInteractorImpl interactor;

    private ActualizarFuncionalidadDtoRequest dtoRequestValido() {
        return ActualizarFuncionalidadDtoRequest.create("funcionalidad", UUID.randomUUID().toString(), "true",
                "2026-01-01 00:00:00", "2026-12-31 23:59:59");
    }

    @Test
    void debeMapearYDelegarEnElCasoDeUsoCuandoElDtoEsValido() {
        var id = UUID.randomUUID();
        var dto = dtoRequestValido();

        interactor.execute(id, dto);

        verify(actualizarFuncionalidad).execute(any(ActualizarFuncionalidadDomain.class));
    }

    @Test
    void debeRelanzarLaExcepcionCuandoElCasoDeUsoFalla() {
        var id = UUID.randomUUID();
        var dto = dtoRequestValido();
        org.mockito.Mockito.doThrow(ValidationException.build("error al actualizar funcionalidad"))
                .when(actualizarFuncionalidad).execute(any(ActualizarFuncionalidadDomain.class));

        assertThrows(ValidationException.class, () -> interactor.execute(id, dto));
    }
}