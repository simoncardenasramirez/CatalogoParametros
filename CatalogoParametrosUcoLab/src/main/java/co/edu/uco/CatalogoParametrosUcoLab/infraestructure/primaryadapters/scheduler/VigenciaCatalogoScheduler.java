package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.ControlEstadoCatalogo;

@Component
@ConditionalOnProperty(name = "catalogo.vigencia.habilitada", havingValue = "true", matchIfMissing = true)
public final class VigenciaCatalogoScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(VigenciaCatalogoScheduler.class);
    private final ControlEstadoCatalogo interactor;
    public VigenciaCatalogoScheduler(final ControlEstadoCatalogo interactor) { this.interactor = interactor; }

    @Scheduled(scheduler = "vigenciaTaskScheduler", fixedDelayString = "${catalogo.vigencia.intervalo:60000}",
            initialDelayString = "${catalogo.vigencia.demora-inicial:10000}")
    public void verificar() {
        try { interactor.sincronizar(); }
        catch (final RuntimeException exception) {
            LOGGER.error("[VIGENCIA-CATALOGO] Fallo la sincronizacion; se reintentara en el siguiente ciclo", exception);
        }
    }
}
