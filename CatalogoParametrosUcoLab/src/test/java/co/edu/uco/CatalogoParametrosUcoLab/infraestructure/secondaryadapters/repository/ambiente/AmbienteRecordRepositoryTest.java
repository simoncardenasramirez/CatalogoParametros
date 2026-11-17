package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.ambiente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteCatalogoRecord;

class AmbienteRecordRepositoryTest {

    private final AmbienteRecordRepository repository = new AmbienteRecordRepository();

    @Test
    void debeConsultarLosTresAmbientesFijosCuandoSeConsultaElCatalogo() {
        var ambientes = repository.findAll();

        assertEquals(3, ambientes.size());
        assertTrue(ambientes.stream().allMatch(ambiente -> ambiente.isActivo()));
        assertEquals("Producción", ambientes.get(0).getNombre());
        assertEquals("Desarrollo", ambientes.get(1).getNombre());
        assertEquals("Pruebas", ambientes.get(2).getNombre());
    }

    @Test
    void debeEncontrarElAmbientePorSuIdCuandoElIdPerteneceAlRecord() {
        var ambiente = repository.findById(AmbienteCatalogoRecord.ID_DESARROLLO);

        assertTrue(ambiente.isPresent());
        assertEquals("Desarrollo", ambiente.orElseThrow().getNombre());
    }

    @Test
    void debeRetornarVacioCuandoElIdNoPerteneceAlRecord() {
        assertTrue(repository.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void debePaginarElCatalogoFijoCuandoSeSolicitaUnaPagina() {
        var ambientes = repository.findAllPaginado(2, 2);

        assertEquals(1, ambientes.size());
        assertEquals("Pruebas", ambientes.get(0).getNombre());
    }
}
