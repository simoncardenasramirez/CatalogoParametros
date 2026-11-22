package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.scheduler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.ControlEstadoCatalogo;

class VigenciaCatalogoSchedulerTest {

    @Test
    void debeSincronizarElCatalogo() {
        final var control = mock(ControlEstadoCatalogo.class);
        final var scheduler = new VigenciaCatalogoScheduler(control);

        scheduler.verificar();

        verify(control).sincronizar();
    }

    @Test
    void debeContinuarCuandoLaSincronizacionFalla() {
        final var control = mock(ControlEstadoCatalogo.class);
        doThrow(new IllegalStateException("fallo temporal")).when(control).sincronizar();
        final var scheduler = new VigenciaCatalogoScheduler(control);

        assertDoesNotThrow(scheduler::verificar);
    }
}
