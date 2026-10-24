package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.metadato;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.MetadatoCacheMapper;
import tools.jackson.databind.node.JsonNodeFactory;

class MetadatoAmbientePersistenciaTest {
    @Test void debePersistirLaRelacionEnTransaccionCuandoCreaOActualiza() {
        var client=mock(SurrealDbClient.class); var repository=new SurrealDbMetadatoRepository(client);
        var ambiente=UUID.randomUUID();
        var entity=MetadatoEntity.create(UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),ambiente,JsonNodeFactory.instance.stringNode("valor"));
        repository.save(entity); repository.update(entity);
        var sql=ArgumentCaptor.forClass(String.class); verify(client,times(2)).execute(sql.capture());
        for(var texto:sql.getAllValues()) {
            assertTrue(texto.contains("idAmbiente: '"+ambiente+"'"));
            assertTrue(texto.contains("BEGIN TRANSACTION;")); assertTrue(texto.contains("COMMIT TRANSACTION;"));
        }
        assertEquals(ambiente,MetadatoCacheMapper.INSTANCE.toEntity(MetadatoCacheMapper.INSTANCE.toData(entity)).getIdAmbiente());
    }
    @Test void debeLeerElAmbienteYConservarHistoricosSinAsignarCuandoConsulta() {
        var client=mock(SurrealDbClient.class); var repository=new SurrealDbMetadatoRepository(client); var factory=JsonNodeFactory.instance;
        var id=UUID.randomUUID(); var ambiente=UUID.randomUUID(); var node=factory.objectNode();
        node.put("id","metadatos:"+id); node.put("idParametro",UUID.randomUUID().toString()); node.put("idTipoMetadato",UUID.randomUUID().toString()); node.put("idAmbiente",ambiente.toString());
        var response=factory.arrayNode().add(factory.objectNode().set("result",factory.arrayNode().add(node)));
        when(client.execute(anyString())).thenReturn(response);
        assertEquals(ambiente,repository.findAll().get(0).getIdAmbiente());
        node.remove("idAmbiente"); assertNull(repository.findAll().get(0).getIdAmbiente());
    }
}
