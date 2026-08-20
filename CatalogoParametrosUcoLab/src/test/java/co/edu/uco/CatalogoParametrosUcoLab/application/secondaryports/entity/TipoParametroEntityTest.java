package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class TipoParametroEntityTest {

    @Test
    void debeCrearConLosValoresDadosCuandoSonValidos() {
        var id = UUID.randomUUID();
        var entity = TipoParametroEntity.create(id, "Texto");

        assertEquals(id, entity.getId());
        assertEquals("Texto", entity.getNombre());
    }

    @Test
    void debeAsignarElIdPorDefectoCuandoElIdEsNulo() {
        var entity = TipoParametroEntity.create(null, "Numero");

        assertEquals(UUIDHelper.getDefault(), entity.getId());
    }

    @Test
    void debeNormalizarElNombreCuandoTieneEspaciosAlInicioYAlFinal() {
        var entity = TipoParametroEntity.create(UUID.randomUUID(), "  Texto  ");

        assertEquals("Texto", entity.getNombre());
    }

    @Test
    void debeAsignarNombreVacioCuandoElNombreEsNulo() {
        var entity = TipoParametroEntity.create(UUID.randomUUID(), null);

        assertEquals(TextHelper.EMPTY, entity.getNombre());
    }

    @Test
    void debeActualizarElIdYElNombreConLosSetters() {
        var entity = TipoParametroEntity.create(UUID.randomUUID(), "Texto");
        var nuevoId = UUID.randomUUID();

        entity.setId(nuevoId);
        entity.setNombre("  Numero  ");

        assertEquals(nuevoId, entity.getId());
        assertEquals("Numero", entity.getNombre());
    }
}