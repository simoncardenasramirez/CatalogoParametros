package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import static org.junit.jupiter.api.Assertions.*;

class VigenciaConfigTest {
    @Test void debeEjecutarEnUnHiloDedicadoCuandoProgramaLaRevision() throws Exception {
        try (var contexto=new AnnotationConfigApplicationContext(VigenciaConfig.class)) {
            var scheduler=contexto.getBean("vigenciaTaskScheduler",ThreadPoolTaskScheduler.class);
            var nombre=scheduler.submit(()->Thread.currentThread().getName()).get(2,TimeUnit.SECONDS);
            assertTrue(nombre.startsWith("vigencia-catalogo-"));
            assertEquals(1,scheduler.getScheduledThreadPoolExecutor().getCorePoolSize());
        }
    }
}
