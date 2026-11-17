package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.tipoparametro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroCatalogoRecord;

class TipoParametroRecordRepositoryTest {

    private final TipoParametroRecordRepository repository = new TipoParametroRecordRepository();

    @Test
    void debeConsultarLosDosTiposFijosCuandoSeConsultaElCatalogo() {
        var tipos = repository.findAll();

        assertEquals(2, tipos.size());
        assertEquals("Técnico", tipos.get(0).getNombre());
        assertEquals("Funcional", tipos.get(1).getNombre());
    }

    @Test
    void debeEncontrarElTipoPorSuIdCuandoElIdPerteneceAlRecord() {
        var tipo = repository.findById(TipoParametroCatalogoRecord.ID_TECNICO);

        assertTrue(tipo.isPresent());
        assertEquals("Técnico", tipo.orElseThrow().getNombre());
    }

    @Test
    void debeRetornarVacioCuandoElIdNoPerteneceAlRecord() {
        assertTrue(repository.findById(UUID.randomUUID()).isEmpty());
    }
}
