package co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.interactor.CambiarEstadoInteractor.TipoRecurso;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class CambiarEstadoInteractorImplTest {

    @Mock private AplicacionRepository aplicacionRepository;
    @Mock private ModuloRepository moduloRepository;
    @Mock private FuncionalidadRepository funcionalidadRepository;
    @Mock private ParametroRepository parametroRepository;
    @Mock private ActualizarAplicacionPublisher aplicacionPublisher;
    @Mock private ActualizarModuloPublisher moduloPublisher;
    @Mock private ActualizarFuncionalidadPublisher funcionalidadPublisher;
    @Mock private ActualizarParametroPublisher parametroPublisher;

    private CambiarEstadoInteractorImpl interactor;

    @BeforeEach
    void setUp() {
        interactor = new CambiarEstadoInteractorImpl(aplicacionRepository, moduloRepository,
                funcionalidadRepository, parametroRepository, aplicacionPublisher, moduloPublisher,
                funcionalidadPublisher, parametroPublisher);
    }

    @Test
    void debeCambiarEstadoDeAplicacion() {
        var id = UUID.randomUUID();
        var entity = AplicacionEntity.create(id, "Aplicacion", UUID.randomUUID(), true,
                OffsetDateTime.now(), null);
        when(aplicacionRepository.findById(id)).thenReturn(Optional.of(entity));
        when(aplicacionRepository.update(entity)).thenReturn(entity);

        interactor.execute(TipoRecurso.APLICACION, id, false);

        assertFalse(entity.isActiva());
        verify(aplicacionRepository).update(entity);
        verify(aplicacionPublisher).sendEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void debeCambiarEstadoDeModulo() {
        var id = UUID.randomUUID();
        var entity = ModuloEntity.create(id, "Modulo", UUID.randomUUID(), true, OffsetDateTime.now(), null);
        when(moduloRepository.findById(id)).thenReturn(Optional.of(entity));
        when(moduloRepository.update(entity)).thenReturn(entity);
        interactor.execute(TipoRecurso.MODULO, id, false);
        assertFalse(entity.isActivo());
        verify(moduloPublisher).sendEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void debeCambiarEstadoDeFuncionalidad() {
        var id = UUID.randomUUID();
        var entity = FuncionalidadEntity.create(id, "Funcionalidad", UUID.randomUUID(), true,
                OffsetDateTime.now(), null);
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.of(entity));
        when(funcionalidadRepository.update(entity)).thenReturn(entity);
        interactor.execute(TipoRecurso.FUNCIONALIDAD, id, false);
        assertFalse(entity.isActivo());
        verify(funcionalidadPublisher).sendEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void debeCambiarEstadoDeParametro() {
        var id = UUID.randomUUID();
        var entity = ParametroEntity.create(id, "Parametro", UUID.randomUUID(), UUID.randomUUID(), true);
        when(parametroRepository.findById(id)).thenReturn(Optional.of(entity));
        when(parametroRepository.update(entity)).thenReturn(entity);
        interactor.execute(TipoRecurso.PARAMETRO, id, false);
        assertFalse(entity.isActivo());
        verify(parametroPublisher).sendEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void debeRechazarIdNuloOEstadoNulo() {
        assertThrows(ValidationException.class,
                () -> interactor.execute(TipoRecurso.APLICACION, null, true));
        assertThrows(ValidationException.class,
                () -> interactor.execute(TipoRecurso.APLICACION, UUID.randomUUID(), null));
    }

    @Test
    void debeInformarCuandoElRecursoNoExiste() {
        var id = UUID.randomUUID();
        when(funcionalidadRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> interactor.execute(TipoRecurso.FUNCIONALIDAD, id, false));
    }
}
