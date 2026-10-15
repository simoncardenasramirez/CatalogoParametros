package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class EstadoTokenEntityTest {
    @Test
    void debeCrearEstadoTokenConIdYEstado() {
        var id = UUID.randomUUID();

        var entity = EstadoTokenEntity.create(id, " activo ");

        assertEquals(id, entity.getId());
        assertEquals("activo", entity.getEstado());
    }

    @Test
    void debeAsignarValoresPorDefectoCuandoSonNulos() {
        var entity = EstadoTokenEntity.create(null, null);

        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), entity.getId());
        assertEquals("", entity.getEstado());
    }
}
