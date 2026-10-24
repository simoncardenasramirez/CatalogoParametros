package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.mapper.CrearMetadatoDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto.ActualizarMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor.mapper.ActualizarMetadatoDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.impl.MetadatoAmbienteExistsRuleImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.*;
import tools.jackson.databind.node.JsonNodeFactory;

class MetadatoAmbienteTest {
    @Test void debeExigirAmbienteCuandoCreaOActualizaPorHttp() {
        var p = UUID.randomUUID().toString(); var t = UUID.randomUUID().toString(); var valor = JsonNodeFactory.instance.stringNode("dato");
        assertThrows(ValidationException.class, () -> CrearMetadatoDtoMapper.INSTANCE.toDtoInput(CrearMetadatoDtoRequest.create(p,t,valor)));
        assertThrows(ValidationException.class, () -> ActualizarMetadatoDtoMapper.INSTANCE.toDtoInput(ActualizarMetadatoDtoRequest.create(p,t,valor)));
    }
    @Test void debeConservarAmbienteCuandoMapeaHastaElDominio() {
        var p = UUID.randomUUID().toString(); var t = UUID.randomUUID().toString(); var ambiente = UUID.randomUUID();
        var dto = CrearMetadatoDtoRequest.create(p,t,ambiente.toString(),JsonNodeFactory.instance.stringNode("dato"));
        var domain = CrearMetadatoDtoMapper.INSTANCE.toDomain(CrearMetadatoDtoMapper.INSTANCE.toDtoInput(dto));
        assertEquals(ambiente, domain.getIdAmbiente());
    }
    @Test void debeRechazarAmbienteInexistenteCuandoValidaLaRelacion() {
        var repository = mock(AmbienteRepository.class); var rule = new MetadatoAmbienteExistsRuleImpl(repository);
        assertThrows(ValidationException.class, () -> rule.execute(null));
        var id = UUID.randomUUID(); assertThrows(NotFoundException.class, () -> rule.execute(id));
        when(repository.findById(id)).thenReturn(Optional.of(mock(AmbienteEntity.class)));
        assertDoesNotThrow(() -> rule.execute(id));
    }
}
