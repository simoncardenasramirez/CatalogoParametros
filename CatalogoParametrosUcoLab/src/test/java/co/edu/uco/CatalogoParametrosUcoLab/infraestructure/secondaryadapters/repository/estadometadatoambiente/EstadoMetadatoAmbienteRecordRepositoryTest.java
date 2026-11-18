package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.estadometadatoambiente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteCatalogoRecord;

class EstadoMetadatoAmbienteRecordRepositoryTest {

    private final EstadoMetadatoAmbienteRecordRepository repository = new EstadoMetadatoAmbienteRecordRepository();

    @Test
    void debeConsultarLosEstadosFijosCuandoSeConsultaElCatalogo() {
        var estados = repository.findAll();

        assertEquals(2, estados.size());
        assertEquals(EstadoMetadatoAmbienteCatalogoRecord.ID_ACTIVO, estados.get(0).getId());
        assertEquals("Activo", estados.get(0).getNombre());
        assertEquals(EstadoMetadatoAmbienteCatalogoRecord.ID_INACTIVO, estados.get(1).getId());
        assertEquals("Inactivo", estados.get(1).getNombre());
    }

    @Test
    void debeEncontrarElEstadoPorSuIdCuandoElIdPerteneceAlRecord() {
        var estado = repository.findById(EstadoMetadatoAmbienteCatalogoRecord.ID_ACTIVO);

        assertTrue(estado.isPresent());
        assertEquals("Activo", estado.orElseThrow().getNombre());
    }

    @Test
    void debeRetornarVacioCuandoElIdNoPerteneceAlRecord() {
        assertTrue(repository.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void debePaginarElCatalogoFijoCuandoSeSolicitaUnaPagina() {
        var estados = repository.findAllPaginado(2, 1);

        assertEquals(1, estados.size());
        assertEquals("Inactivo", estados.get(0).getNombre());
    }
}
