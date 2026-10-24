package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import org.junit.jupiter.api.Test;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.ControlEstadoCatalogo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisCatalogosWiringTest {
    private static final String PAQUETE = "co.edu.uco.CatalogoParametrosUcoLab.";
    private static final String[] CATALOGOS = { "Parametro", "Organizacion", "Aplicacion", "Modulo",
            "Funcionalidad", "Metadato", "TipoParametro", "TipoMetadato", "Ambiente", "EstadoAmbiente",
            "MetadatoAmbiente", "EstadoMetadatoAmbiente" };

    @Test
    void debeResolverTodosLosPuertosSinAmbiguedadCuandoRegistraCatalogosCompletos() throws ClassNotFoundException {
        try (var contexto = new AnnotationConfigApplicationContext()) {
            contexto.registerBean(ControlEstadoCatalogo.class, () -> mock(ControlEstadoCatalogo.class));
            contexto.registerBean(StringRedisTemplate.class, () -> mock(StringRedisTemplate.class));
            contexto.registerBean(SurrealDbClient.class, () -> mock(SurrealDbClient.class));
            contexto.registerBean(RedisCacheProperties.class, RedisCacheProperties::new);
            for (var nombre : CATALOGOS) {
                contexto.register(estrategia(nombre), baseDatos(nombre), cache(nombre));
            }
            contexto.refresh();
            for (var nombre : CATALOGOS) {
                assertInstanceOf(estrategia(nombre), contexto.getBean(puerto(nombre + "Repository")));
                assertInstanceOf(baseDatos(nombre), contexto.getBean(puerto("BaseDatos" + nombre + "Repository")));
                assertInstanceOf(cache(nombre), contexto.getBean(puerto("Cache" + nombre + "Repository")));
            }
        }
    }

    private Class<?> puerto(final String nombre) throws ClassNotFoundException {
        return Class.forName(PAQUETE + "application.secondaryports.repository." + nombre);
    }

    private Class<?> estrategia(final String nombre) throws ClassNotFoundException {
        return Class.forName(PAQUETE + "application.catalogo.estrategia." + nombre + "CatalogoStrategy");
    }

    private Class<?> baseDatos(final String nombre) throws ClassNotFoundException {
        return Class.forName(PAQUETE + "infraestructure.secondaryadapters.repository." + nombre.toLowerCase()
                + ".SurrealDb" + nombre + "Repository");
    }

    private Class<?> cache(final String nombre) throws ClassNotFoundException {
        return Class.forName(PAQUETE + "infraestructure.secondaryadapters.repository.redis.Redis" + nombre + "Repository");
    }
}
