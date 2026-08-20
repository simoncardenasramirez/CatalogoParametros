package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SurrealDbPropertiesTest {

    @Test
    void debeIniciarConValoresNulosCuandoSeCreaLaInstancia() {
        var properties = new SurrealDbProperties();

        assertNull(properties.getUrl());
        assertNull(properties.getNamespace());
        assertNull(properties.getDatabase());
        assertNull(properties.getUsername());
        assertNull(properties.getPassword());
    }

    @Test
    void debeGuardarYRecuperarCadaPropiedadConfigurada() {
        var properties = new SurrealDbProperties();
        properties.setUrl("http://localhost:8000");
        properties.setNamespace("uco");
        properties.setDatabase("catalogo_parametros");
        properties.setUsername("root");
        properties.setPassword("root");

        assertEquals("http://localhost:8000", properties.getUrl());
        assertEquals("uco", properties.getNamespace());
        assertEquals("catalogo_parametros", properties.getDatabase());
        assertEquals("root", properties.getUsername());
        assertEquals("root", properties.getPassword());
    }
}