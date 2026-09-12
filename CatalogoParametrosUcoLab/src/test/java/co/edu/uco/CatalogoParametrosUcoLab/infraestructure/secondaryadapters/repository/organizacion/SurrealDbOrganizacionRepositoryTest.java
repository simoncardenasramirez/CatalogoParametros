package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.organizacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

class SurrealDbOrganizacionRepositoryTest {
    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    
    private static final OffsetDateTime FECHA = OffsetDateTime.of(2026, 1, 2, 10, 30, 0, 0, ZoneOffset.of("-05:00"));
    private final SurrealDbClient cliente = mock(SurrealDbClient.class);
    private final SurrealDbOrganizacionRepository repositorio = new SurrealDbOrganizacionRepository(cliente);
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void debeGuardarTransaccionEscapadaCuandoNombreContieneCaracteresEspeciales() {
        var entidad = OrganizacionEntity.create(ID, "O'Reilly\\UCO", FECHA, null);
        assertSame(entidad, repositorio.save(entidad));
        var consulta = ArgumentCaptor.forClass(String.class);
        verify(cliente).execute(consulta.capture());
        assertAll(
                () -> assertTrue(consulta.getValue().startsWith("BEGIN TRANSACTION;")),
                () -> assertTrue(consulta.getValue().contains("CREATE type::record('organizaciones', '" + ID + "')")),
                () -> assertTrue(consulta.getValue().contains("O\\'Reilly\\\\UCO")),
                
                () -> assertTrue(consulta.getValue().contains("fechaInicio: '2026-01-02T10:30-05:00'")),
                () -> assertTrue(consulta.getValue().contains("fechaFinal: null")),
                () -> assertTrue(consulta.getValue().stripTrailing().endsWith("COMMIT TRANSACTION;")));
    }

    @Test
    void debeActualizarDatosCuandoEntidadExiste() {
        var entidad = OrganizacionEntity.create(ID, "Actualizado", null, FECHA);
        assertSame(entidad, repositorio.update(entidad));
        var consulta = ArgumentCaptor.forClass(String.class);
        verify(cliente).execute(consulta.capture());
        assertAll(
                () -> assertTrue(consulta.getValue().contains("UPDATE type::record('organizaciones', '" + ID + "')")),
                () -> assertTrue(consulta.getValue().contains("nombre: 'Actualizado'")),
                
                () -> assertTrue(consulta.getValue().contains("fechaInicio: null")),
                () -> assertTrue(consulta.getValue().contains("fechaFinal: '2026-01-02T10:30-05:00'")),
                () -> assertTrue(consulta.getValue().contains("COMMIT TRANSACTION;")));
    }

    @Test
    void debeEliminarRegistroCorrectoCuandoRecibeIdentificador() {
        repositorio.deleteById(ID);
        var consulta = ArgumentCaptor.forClass(String.class);
        verify(cliente).execute(consulta.capture());
        assertTrue(consulta.getValue().contains("DELETE type::record('organizaciones', '" + ID + "');"));
    }

    @Test
    void debeConsultarUltimoResultadoCuandoRespuestaContieneVariasSentencias() {
        responder(registro());
        var entidad = repositorio.findById(ID).orElseThrow();
        assertAll(
                () -> assertEquals(ID, entidad.getId()),
                () -> assertEquals("Catalogo", entidad.getNombre()),
                
                () -> assertEquals(FECHA, entidad.getFechaInicio()),
                () -> assertNull(entidad.getFechaFinal()));
        verify(cliente).execute("SELECT * FROM organizaciones:`" + ID + "`;");
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "[]", "[{}]", "[{\"result\":null}]", "[{\"result\":[]}]"})
    void debeRetornarAusenciaCuandoNoHayRegistros(final String respuesta) {
        when(cliente.execute(anyString())).thenReturn(mapper.readTree(respuesta));
        assertTrue(repositorio.findById(ID).isEmpty());
        assertTrue(repositorio.findAll().isEmpty());
        assertTrue(repositorio.findAllPaginado(1, 10).isEmpty());
        assertFalse(repositorio.existsByNombre("Ausente"));
        
    }

    @Test
    void debeListarRegistrosCuandoConsultaCatalogo() {
        responder(registro(), registro().put("id", "33333333-3333-3333-3333-333333333333"));
        var entidades = repositorio.findAll();
        assertEquals(2, entidades.size());
        assertEquals(ID, entidades.get(0).getId());
        assertEquals(UUID.fromString("33333333-3333-3333-3333-333333333333"), entidades.get(1).getId());
        verify(cliente).execute("SELECT * FROM organizaciones;");
    }

    @Test
    void debeAplicarLimiteYDesplazamientoCuandoConsultaPagina() {
        responder(registro());
        var entidades = repositorio.findAllPaginado(3, 5);
        assertEquals(1, entidades.size());
        assertEquals(ID, entidades.getFirst().getId());
        verify(cliente).execute("SELECT * FROM organizaciones LIMIT 5 START 10;");
    }

    @Test
    void debeDetectarExistenciaCuandoNombreTieneComilla() {
        responder(registro());
        assertTrue(repositorio.existsByNombre("O'Reilly"));
        verify(cliente).execute("SELECT id FROM organizaciones WHERE nombre = 'O\\'Reilly' LIMIT 1;");
    }

    

    @ParameterizedTest
    @ValueSource(strings = {"", "identificador-invalido"})
    void debeUsarUuidPredeterminadoCuandoIdentificadorNoEsValido(final String identificador) {
        var registro = registro().put("id", identificador);
        
        responder(registro);
        var entidad = repositorio.findById(ID).orElseThrow();
        assertEquals(new UUID(0, 0), entidad.getId());
        
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026-01-02T10:30-05:00", "2026-01-02T10:30:00-05:00", "d'2026-01-02T10:30:00-05:00'"})
    void debeNormalizarFechaCuandoSurrealDevuelveFormatosSoportados(final String fecha) {
        responder(registro().put("fechaInicio", fecha).put("fechaFinal", fecha));
        var entidad = repositorio.findById(ID).orElseThrow();
        assertEquals(FECHA, entidad.getFechaInicio());
        assertEquals(FECHA, entidad.getFechaFinal());
    }

    @Test
    void debeRetornarFechaNulaCuandoCampoEstaVacio() {
        responder(registro().put("fechaInicio", "").putNull("fechaFinal"));
        var entidad = repositorio.findById(ID).orElseThrow();
        assertNull(entidad.getFechaInicio());
        assertNull(entidad.getFechaFinal());
    }

    @Test
    void debePropagarFalloCuandoClienteRechazaConsulta() {
        var error = TechnicalException.build("Error de persistencia");
        when(cliente.execute(anyString())).thenThrow(error);
        assertSame(error, assertThrows(TechnicalException.class, repositorio::findAll));
    }

    private ObjectNode registro() {
        var registro = mapper.createObjectNode()
                .put("id", "organizaciones:`" + ID + "`")
                .put("nombre", "Catalogo")
                .put("fechaInicio", "d'2026-01-02T10:30-05:00'")
                .putNull("fechaFinal");
        
        return registro;
    }

    private void responder(final ObjectNode... registros) {
        var respuesta = mapper.createArrayNode();
        respuesta.addObject().putArray("result"); // Una sentencia previa no contiene las filas consultadas.
        var filas = respuesta.addObject().putArray("result");
        for (var registro : registros) {
            filas.add(registro);
        }
        when(cliente.execute(anyString())).thenReturn(respuesta);
    }
}
