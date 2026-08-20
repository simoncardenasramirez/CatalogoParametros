package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.CrearFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class CrearFuncionalidadInteractorImplTest {

    @Mock
    private CrearFuncionalidad crearFuncionalidad;

    @InjectMocks
    private CrearFuncionalidadInteractorImpl interactor;

    private CrearFuncionalidadDtoRequest dtoRequestValido() {
        return CrearFuncionalidadDtoRequest.create("funcionalidad", UUID.randomUUID().toString(), "true",
                "2026-01-01 00:00:00", "2026-12-31 23:59:59");
    }

    @Test
    void debeMapearYDelegarEnElCasoDeUsoCuandoElDtoEsValido() {
        var dto = dtoRequestValido();

        interactor.execute(dto);

        verify(crearFuncionalidad).execute(any(CrearFuncionalidadDomain.class));
    }

    @Test
    void debeRelanzarLaExcepcionCuandoElCasoDeUsoFalla() {
        var dto = dtoRequestValido();
        org.mockito.Mockito.doThrow(ValidationException.build("error al crear funcionalidad"))
                .when(crearFuncionalidad).execute(any(CrearFuncionalidadDomain.class));

        assertThrows(ValidationException.class, () -> interactor.execute(dto));
    }
}